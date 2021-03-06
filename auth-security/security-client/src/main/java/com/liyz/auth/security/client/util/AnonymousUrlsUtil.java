package com.liyz.auth.security.client.util;

import com.google.common.collect.Lists;
import com.liyz.auth.security.base.annotation.Anonymous;
import com.liyz.auth.security.base.annotation.NonAuthority;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * 注释:免鉴权url注解工具类
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/4/13 9:30
 */
@Slf4j
@Component
public class AnonymousUrlsUtil implements ApplicationContextAware, Ordered {

    private static final String ASTERISK = "**";

    //免登陆访问
    private static volatile List<String> anonymousUrls;
    //免授权访问
    private static volatile List<String> nonAuthorityUrls;

    private static ApplicationContext applicationContext;

    /**
     * 获取免鉴权的urls {@link Anonymous}
     *
     * @return
     * @throws ClassNotFoundException
     */
    public static List<String> anonymousUrls() {
        if (CollectionUtils.isEmpty(anonymousUrls)) {
            synchronized (AnonymousUrlsUtil.class) {
                if (CollectionUtils.isEmpty(anonymousUrls)) {
                    anonymousUrls = Lists.newArrayList();
                    Map<String, Object> map = applicationContext.getBeansWithAnnotation(Controller.class);
                    if (CollectionUtils.isEmpty(map)) {
                        return anonymousUrls;
                    }
                    Class beanClass;
                    for (Object bean : map.values()) {
                        //获取原始类而不是代理类
                        beanClass = AopUtils.isAopProxy(bean) ? AopUtils.getTargetClass(bean) : bean.getClass();
                        scanMethods(beanClass, Anonymous.class, anonymousUrls);
                    }
                }
            }
        }
        return anonymousUrls;
    }

    /**
     * 获取免鉴权的urls {@link NonAuthority}
     *
     * @return
     * @throws ClassNotFoundException
     */
    public static List<String> nonAuthorityUrls() {
        if (CollectionUtils.isEmpty(nonAuthorityUrls)) {
            synchronized (AnonymousUrlsUtil.class) {
                if (CollectionUtils.isEmpty(nonAuthorityUrls)) {
                    nonAuthorityUrls = Lists.newArrayList();
                    Map<String, Object> map = applicationContext.getBeansWithAnnotation(Controller.class);
                    if (CollectionUtils.isEmpty(map)) {
                        return nonAuthorityUrls;
                    }
                    Class beanClass;
                    for (Object bean : map.values()) {
                        //获取原始类而不是代理类
                        beanClass = AopUtils.isAopProxy(bean) ? AopUtils.getTargetClass(bean) : bean.getClass();
                        scanMethods(beanClass, NonAuthority.class, nonAuthorityUrls);
                    }
                }
            }
        }
        return nonAuthorityUrls;
    }

    /**
     * 获取类上的注解 {@link RequestMapping} 的value
     *
     * @param beanClass
     * @return
     */
    private static String classMapping(Class beanClass) {
        String classMapping = "";
        if (beanClass.isAnnotationPresent(RequestMapping.class)) {
            RequestMapping requestMapping = (RequestMapping) beanClass.getAnnotation(RequestMapping.class);
            String[] mappings = requestMapping.value();
            if (mappings != null && mappings.length > 0) {
                classMapping = mappings[0];
            }
        }
        return classMapping;
    }

    /**
     * 获取方法上的mapping
     *
     * @param method
     * @return
     */
    private static String methodMapping(Method method) {
        String[] methodMappings = null;
        if (method.isAnnotationPresent(RequestMapping.class)) {
            methodMappings = method.getAnnotation(RequestMapping.class).value();
        } else if (method.isAnnotationPresent(GetMapping.class)) {
            methodMappings = method.getAnnotation(GetMapping.class).value();
        } else if (method.isAnnotationPresent(PutMapping.class)) {
            methodMappings = method.getAnnotation(PutMapping.class).value();
        } else if (method.isAnnotationPresent(PostMapping.class)) {
            methodMappings = method.getAnnotation(PostMapping.class).value();
        } else if (method.isAnnotationPresent(DeleteMapping.class)) {
            methodMappings = method.getAnnotation(DeleteMapping.class).value();
        }
        if (methodMappings != null && methodMappings.length > 0) {
            String mapping =  methodMappings[0];
            if (mapping.contains("{")) {
                mapping = mapping.substring(0, mapping.indexOf("{")) + ASTERISK;
            }
            return mapping;
        }
        return null;
    }

    /**
     * 扫描类上的方法获取url
     *
     * @param beanClass
     * @param annotationClass
     * @param anonymousUrls
     */
    private static void scanMethods(Class beanClass, Class<? extends Annotation> annotationClass, List<String> anonymousUrls) {
        String classMapping = classMapping(beanClass);
        boolean classHaveAnonymous = beanClass.isAnnotationPresent(annotationClass);
        Method[] methods = beanClass.getDeclaredMethods();
        if (methods != null && methods.length > 0) {
            for (Method method : methods) {
                boolean methodHaveAnonymous = classHaveAnonymous ? classHaveAnonymous : method.isAnnotationPresent(annotationClass);
                if (methodHaveAnonymous) {
                    String methodMapping = methodMapping(method);
                    if (StringUtils.isNotBlank(methodMapping)) {
                        anonymousUrls.add(classMapping + methodMapping);
                    }
                }
            }
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
