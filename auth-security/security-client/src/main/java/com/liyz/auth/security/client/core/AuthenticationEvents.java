package com.liyz.auth.security.client.core;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import java.io.IOException;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/4/23 14:51
 */
@Slf4j
@Component
public class AuthenticationEvents {

    private RestfulAuthenticationFailureHandler failureHandler = new RestfulAuthenticationFailureHandler();

    @EventListener
    public void onSuccess(AuthenticationSuccessEvent success) {
        log.info("auth success");
    }

    @EventListener
    public void onFailure(AuthenticationFailureBadCredentialsEvent failures) throws ServletException, IOException {
        log.error("auth fail");
    }
}
