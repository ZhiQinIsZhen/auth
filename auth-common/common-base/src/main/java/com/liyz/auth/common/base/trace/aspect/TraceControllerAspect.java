package com.liyz.auth.common.base.trace.aspect;

import com.liyz.auth.common.base.constant.CommonConstant;
import com.liyz.auth.common.base.trace.TraceContext;
import com.liyz.auth.common.base.trace.TraceInfo;
import com.liyz.auth.common.base.trace.util.AspectUtil;
import com.liyz.auth.common.base.util.JsonMapperUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

/**
 * 注释:链路切面
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/7/8 20:25
 */
@Slf4j
@Aspect
@Configuration
@Order(2)
public class TraceControllerAspect {

    /**
     * 切点
     */
    @Pointcut("execution(public com.liyz.auth.common.base.result.Result com.liyz.*.controller.*.*(..))")
    public void controllerAspect() {}

    /**
     * 环绕切面
     *
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around("controllerAspect()")
    public Object controllerAround(ProceedingJoinPoint joinPoint) throws Throwable {
        String paramsStr = AspectUtil.getAspectMethodParams(joinPoint);
        TraceInfo traceInfo = new TraceInfo();
        traceInfo.setTraceId(TraceContext.createTraceId());
        traceInfo.setSpanId(TraceContext.createSpanId());
        traceInfo.setParams(paramsStr);
        traceInfo.setMethod(joinPoint.getTarget().getClass().getSimpleName().concat(CommonConstant.METHOD_SPLIT)
                .concat(joinPoint.getSignature().getName()));
        TraceContext.setTraceInfo(traceInfo);
        log.info("traceId : {}, spanId : {}, method : {}, params : {}",
                traceInfo.getTraceId(), traceInfo.getSpanId(), traceInfo.getMethod(), traceInfo.getParams());
        Object obj = joinPoint.proceed();
        traceInfo.setDuration(System.currentTimeMillis() - traceInfo.getTimestamp());
        traceInfo.setResult(JsonMapperUtil.toJSONString(obj));
        log.info("traceId : {}, spanId : {}, method : {} ; duration : {} ms ; response result : {}",
                traceInfo.getTraceId(), traceInfo.getSpanId(), traceInfo.getMethod(), traceInfo.getDuration(), traceInfo.getResult());
        TraceContext.removeTraceInfo();
        return obj;
    }

    /**
     * 异常切面
     *
     * @param joinPoint
     * @param ex
     */
    @AfterThrowing(pointcut = "controllerAspect()", throwing = "ex")
    public void afterThrowing(JoinPoint joinPoint, Throwable ex) {
        TraceInfo traceInfo = TraceContext.getTraceInfo();
        traceInfo.setDuration(System.currentTimeMillis() - traceInfo.getTimestamp());
        traceInfo.setIsNormal(Boolean.FALSE);
        log.error("traceId : {}, spanId : {}, method : {} ; duration : {} ms ; response result : {}",
                traceInfo.getTraceId(), traceInfo.getSpanId(), traceInfo.getMethod(), traceInfo.getDuration(), traceInfo.getResult());
        TraceContext.removeTraceInfo();
    }
}
