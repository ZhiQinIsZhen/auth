package com.liyz.auth.security.client.config;

import com.liyz.auth.common.util.JsonMapperUtil;
import com.liyz.auth.security.base.constant.SecurityConstant;
import com.liyz.auth.security.client.core.AccessDecisionManagerImpl;
import com.liyz.auth.security.client.core.JwtAuthenticationEntryPoint;
import com.liyz.auth.security.client.core.RestfulAccessDeniedHandler;
import com.liyz.auth.security.client.filter.GrantedAuthoritySecurityInterceptor;
import com.liyz.auth.security.client.filter.JwtAuthenticationTokenFilter;
import com.liyz.auth.security.client.util.AnonymousUrlsUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DefaultAuthenticationEventPublisher;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.annotation.Resource;
import java.util.List;

/**
 * ??????:Security?????????
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/4/13 10:00
 */
@Slf4j
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityClientConfig extends WebSecurityConfigurerAdapter {

    @Value("${jwt.user.authority:false}")
    private boolean authority;
    @Value("${jwt.tokenHeader.key:Authorization}")
    private String tokenHeaderKey;

    @Resource
    private PasswordEncoder passwordEncoder;
    @Resource
    private UserDetailsService userDetailsService;

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Autowired
    public void configureAuthentication(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        List<String> list = AnonymousUrlsUtil.anonymousUrls();
        log.info("Anonymous api:{}", JsonMapperUtil.toJSONString(list));
        http
                //??????????????????JWT????????????????????????csrf????????????entryPoint
                .csrf()
                .disable()
                .exceptionHandling()
                .accessDeniedHandler(new RestfulAccessDeniedHandler())
                .authenticationEntryPoint(new JwtAuthenticationEntryPoint()).and()
//                .requestMatcher(new RequestedMatcherImpl())
                //??????token??????????????????session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests()
                //??????????????????
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                //??????????????????swagger????????????????????????
                .antMatchers(HttpMethod.GET, SecurityConstant.SECURITY_IGNORE_RESOURCES).permitAll()
                //???????????????????????????api
                .antMatchers(list.toArray(new String[list.size()])).permitAll()
                //???????????????????????????
                .anyRequest().authenticated().and()
                //??????jwt?????????
                .addFilterBefore(
                        new JwtAuthenticationTokenFilter(tokenHeaderKey, userDetailsService),
                        UsernamePasswordAuthenticationFilter.class)
                // ????????????
                .headers().cacheControl().and()
                //spring security?????????ifame??????????????????
                .frameOptions().sameOrigin();
        if (authority) {
            http.addFilterBefore(new GrantedAuthoritySecurityInterceptor(new AccessDecisionManagerImpl()), FilterSecurityInterceptor.class);
        }
    }

    @Bean
    public AuthenticationEventPublisher authenticationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        return new DefaultAuthenticationEventPublisher(applicationEventPublisher);
    }
}
