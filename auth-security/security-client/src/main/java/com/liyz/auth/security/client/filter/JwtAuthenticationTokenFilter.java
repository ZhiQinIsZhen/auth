package com.liyz.auth.security.client.filter;

import com.liyz.auth.security.client.AuthContext;
import com.liyz.auth.security.base.user.AuthUserDetails;
import org.apache.commons.lang3.StringUtils;
import org.springframework.mobile.device.Device;
import org.springframework.mobile.device.LiteDeviceResolver;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.Objects;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/4/12 15:27
 */
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    private final String tokenHeaderKey;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationTokenFilter(String tokenHeaderKey, UserDetailsService userDetailsService) {
        this.tokenHeaderKey = tokenHeaderKey;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        try {
            //处理request head信息
            String tokenHeaderKey = httpServletRequest.getHeader(this.tokenHeaderKey);
            if (StringUtils.isNotBlank(tokenHeaderKey)) {
                tokenHeaderKey = URLDecoder.decode(tokenHeaderKey, "UTF-8");
                final AuthUserDetails userDetails = (AuthUserDetails) userDetailsService.loadUserByUsername(tokenHeaderKey);
                if (Objects.nonNull(userDetails)) {
                    Device device = new LiteDeviceResolver().resolveDevice(httpServletRequest);
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        } finally {
            AuthContext.remove();
        }
    }
}
