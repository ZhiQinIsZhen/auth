package com.liyz.auth.common.base.trace.aspect;

import com.liyz.auth.common.base.constant.CommonConstant;
import com.liyz.auth.common.base.trace.TraceContext;
import com.liyz.auth.common.base.trace.TraceInfo;
import com.liyz.auth.common.base.trace.util.AspectUtil;
import com.liyz.auth.common.base.util.JsonMapperUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.rpc.RpcContext;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.util.Objects;

/**
 * 注释:链路切面
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/7/9 18:00
 */
@Slf4j
@Aspect
@Configuration
@ConditionalOnExpression("${dubbo.trace.filter:false}")
@Order(2)
public class TraceDubboAspect {

    /**
     * 切点
     */
    @Pointcut("execution(public * com.liyz.auth.*.*.provider.*.*(..))")
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
        String traceId = RpcContext.getContext().getAttachment(CommonConstant.REQUEST_ID);
        String parentSpanId = RpcContext.getContext().getAttachment(CommonConstant.PARENT_SPAN_ID);
        String paramsStr = AspectUtil.getAspectMethodParams(joinPoint);
        TraceInfo traceInfo = new TraceInfo();
        if (StringUtils.isNotBlank(traceId)) {
            traceInfo.setTraceId(traceId);
            traceInfo.setSpanId(TraceContext.createSpanId());
            traceInfo.setParentSpanId(parentSpanId);
            traceInfo.setParams(paramsStr);
            traceInfo.setMethod(joinPoint.getTarget().getClass().getSimpleName().concat(CommonConstant.METHOD_SPLIT)
                    .concat(joinPoint.getSignature().getName()));
            TraceContext.setTraceInfo(traceInfo);
            log.info("traceId : {}, spanId : {}, parentSpanId : {}, method : {}, params : {}", traceInfo.getTraceId(),
                    traceInfo.getSpanId(), traceInfo.getParentSpanId(), traceInfo.getMethod(), traceInfo.getParams());
        }
        Object obj = joinPoint.proceed();
        if (StringUtils.isNotBlank(traceId)) {
            traceInfo.setDuration(System.currentTimeMillis() - traceInfo.getTimestamp());
            traceInfo.setResult(JsonMapperUtil.toJSONString(obj));
            log.info("traceId : {}, spanId : {}, parentSpanId : {}, method : {} ; duration : {} ms ; response result : {}",
                    traceInfo.getTraceId(), traceInfo.getSpanId(), traceInfo.getParentSpanId(), traceInfo.getMethod(),
                    traceInfo.getDuration(), traceInfo.getResult());
        }
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
        if (Objects.nonNull(traceInfo)) {
            traceInfo.setDuration(System.currentTimeMillis() - traceInfo.getTimestamp());
            traceInfo.setIsNormal(Boolean.FALSE);
            log.error("traceId : {}, spanId : {}, parentSpanId : {}, method : {} ; duration : {} ms ; response result : {}",
                    traceInfo.getTraceId(), traceInfo.getSpanId(), traceInfo.getParentSpanId(), traceInfo.getMethod(),
                    traceInfo.getDuration(), traceInfo.getResult());
        }
        TraceContext.removeTraceInfo();
    }
}
