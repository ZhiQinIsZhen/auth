//package com.liyz.auth.common.base.trace.aspect;
//
//import com.liyz.auth.common.base.constant.CommonConstant;
//import com.liyz.auth.common.base.trace.TraceContext;
//import com.liyz.auth.common.base.trace.annotation.Logs;
//import com.liyz.auth.common.base.trace.util.AspectUtil;
//import com.liyz.auth.common.base.util.HttpRequestUtil;
//import com.liyz.auth.common.base.util.JsonMapperUtil;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.lang3.StringUtils;
//import org.apache.dubbo.config.annotation.DubboService;
//import org.apache.dubbo.config.annotation.Service;
//import org.aspectj.lang.JoinPoint;
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.AfterThrowing;
//import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Pointcut;
//import org.aspectj.lang.reflect.MethodSignature;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.annotation.Order;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.context.request.RequestContextHolder;
//import org.springframework.web.context.request.ServletRequestAttributes;
//
//import java.lang.reflect.Method;
//import java.util.Objects;
//import java.util.UUID;
//
///**
// * 注释:日志切面
// *
// * @author liyangzhen
// * @version 1.0.0
// * @date 2021/7/8 17:33
// */
//@Slf4j
//@ConditionalOnExpression("${spring.logs:true}")
//@Aspect
//@Configuration
//@Order(2)
//public class LogsAspect {
//
//    /**
//     * 切点
//     */
//    @Pointcut("@annotation(com.liyz.auth.common.base.trace.annotation.Logs)")
//    public void aspect() {}
//
//    /**
//     * 环绕切面
//     *
//     * @param joinPoint
//     * @return
//     * @throws Throwable
//     */
//    @Around("aspect()")
//    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
//        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
//        Method method = signature.getMethod();
//        Logs logs = method.getAnnotation(Logs.class);
//        String methodName = StringUtils.isBlank(logs.method())
//                ? new StringBuilder()
//                .append(joinPoint.getTarget().getClass().getSimpleName())
//                .append(CommonConstant.METHOD_SPLIT)
//                .append(joinPoint.getSignature().getName())
//                .toString()
//                : logs.method();
//        Class clazz = joinPoint.getTarget().getClass();
//        int type = clazz.isAnnotationPresent(RestController.class) ? 0 :
//                clazz.isAnnotationPresent(Service.class) || clazz.isAnnotationPresent(DubboService.class) ? 1 : -1;
//        String ip = type == 0
//                ? HttpRequestUtil.getIpAddress(((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest())
//                : null;
//        String traceId = TraceContext.getTraceId();
//        if (StringUtils.isBlank(traceId)) {
//            traceId = UUID.randomUUID().toString().replaceAll("-", "");
//            TraceContext.setTraceId(traceId, methodName);
//        }
//        if (type >= 0 && logs.before()) {
//            log.info("traceId : {}, method : {}, params : {}", traceId, methodName, AspectUtil.getMethodParams(joinPoint));
//        }
//        Object obj = joinPoint.proceed();
//        if (type >= 0 && logs.after()) {
//            log.info("traceId : {}, method : {} ; response result : {}", traceId, methodName, JsonMapperUtil.toJSONString(obj));
//        }
//        return obj;
//    }
//
//    /**
//     * 异常切面
//     *
//     * @param joinPoint
//     * @param ex
//     */
//    @AfterThrowing(pointcut = "aspect()", throwing = "ex")
//    public void afterThrowing(JoinPoint joinPoint, Throwable ex) {
//        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
//        Method method = signature.getMethod();
//        Logs logs = method.getAnnotation(Logs.class);
//        String methodName = StringUtils.isBlank(logs.method())
//                ? new StringBuilder()
//                .append(joinPoint.getTarget().getClass().getSimpleName())
//                .append(CommonConstant.METHOD_SPLIT)
//                .append(joinPoint.getSignature().getName())
//                .toString()
//                : logs.method();
//        String traceId = TraceContext.getTraceId();
//        if (StringUtils.isBlank(traceId)) {
//            traceId = UUID.randomUUID().toString().replaceAll("-", "");
//            TraceContext.setTraceId(traceId, methodName);
//        }
//        if (logs.exception()) {
//            log.error("traceId : {}, method : {} ; exception type : {} ; exception message : {}",
//                    traceId, methodName, ex.getClass().getSimpleName(), ex.getMessage());
//        }
//        TraceContext.removeTraceId();
//    }
//}
