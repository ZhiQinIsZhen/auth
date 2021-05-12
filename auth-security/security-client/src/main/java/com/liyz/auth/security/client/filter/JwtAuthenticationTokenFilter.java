package com.liyz.auth.security.client.filter;

import com.google.common.base.Charsets;
import com.liyz.auth.common.base.result.Result;
import com.liyz.auth.common.remote.exception.RemoteServiceException;
import com.liyz.auth.common.util.JsonMapperUtil;
import com.liyz.auth.security.base.user.AuthUser;
import com.liyz.auth.security.base.user.AuthUserDetails;
import com.liyz.auth.security.client.AuthContext;
import com.liyz.auth.security.client.context.JwtContextHolder;
import com.liyz.auth.security.client.impl.UserDetailsServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
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
@Slf4j
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
            String token = httpServletRequest.getHeader(this.tokenHeaderKey);
            if (StringUtils.isNotBlank(token)) {
                token = URLDecoder.decode(token, String.valueOf(Charsets.UTF_8));
                final AuthUser authUser = JwtContextHolder.getJwtAuthService().loadUserByToken(token);
                if (Objects.nonNull(authUser)) {
                    AuthContext.setAuthUser(authUser);
                    AuthUserDetails authUserDetails = UserDetailsServiceImpl.getByAuthUser(authUser);
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    authUserDetails,
                                    null,
                                    authUserDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        } catch (RemoteServiceException exception) {
            log.error("auth token fail, cause by ==> code : {}, msg : {}", exception.getCode(), exception.getMessage());
            httpServletResponse.setCharacterEncoding(String.valueOf(Charsets.UTF_8));
            httpServletResponse.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
            httpServletResponse.getWriter().println(JsonMapperUtil.toJSONString(Result.error(exception.getCode(),
                    exception.getMessage())));
            httpServletResponse.getWriter().flush();
        } finally {
            AuthContext.remove();
        }
    }
}
