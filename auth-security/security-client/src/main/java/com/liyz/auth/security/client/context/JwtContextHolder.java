package com.liyz.auth.security.client.context;

import com.liyz.auth.common.base.constant.CommonConstant;
import com.liyz.auth.common.base.util.HttpRequestUtil;
import com.liyz.auth.security.base.remote.RemoteGrantedAuthorityService;
import com.liyz.auth.security.base.remote.RemoteJwtAuthService;
import com.liyz.auth.security.base.user.AuthUserDetails;
import com.liyz.auth.security.base.user.ClaimDetail;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.mobile.device.Device;
import org.springframework.mobile.device.LiteDeviceResolver;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.http.HttpServletRequest;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/4/15 9:45
 */
@Configuration
public class JwtContextHolder implements ApplicationContextAware, InitializingBean {

    private static ApplicationContext applicationContext;

    public static RemoteJwtAuthService getJwtAuthService() {
        return applicationContext.getBean("remoteJwtAuthService", RemoteJwtAuthService.class);
    }

    public static RemoteGrantedAuthorityService getGrantedAuthorityService() {
        return applicationContext.getBean("remoteGrantedAuthorityService", RemoteGrantedAuthorityService.class);
    }

    public static String getJWT() {
        HttpServletRequest httpServletRequest = HttpRequestUtil.getRequest();
        LiteDeviceResolver resolver = new LiteDeviceResolver();
        Device device = resolver.resolveDevice(httpServletRequest);
        AuthUserDetails authUserDetails = (AuthUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ClaimDetail claimDetail = new ClaimDetail();
        claimDetail.setDevice(device.isMobile() ? CommonConstant.DEVICE_MOBILE : CommonConstant.DEVICE_WEB);
        claimDetail.setUsername(authUserDetails.getLoginName());
        claimDetail.setUserId(authUserDetails.getId());
        return getJwtAuthService().getJWT(claimDetail);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    private static RemoteJwtAuthService remoteJwtAuthService;
    private static RemoteGrantedAuthorityService remoteGrantedAuthorityService;

    @Override
    public void afterPropertiesSet() throws Exception {
        remoteJwtAuthService = applicationContext.getBean("remoteJwtAuthService", RemoteJwtAuthService.class);
        remoteGrantedAuthorityService = applicationContext.getBean("remoteGrantedAuthorityService", RemoteGrantedAuthorityService.class);
    }
}
