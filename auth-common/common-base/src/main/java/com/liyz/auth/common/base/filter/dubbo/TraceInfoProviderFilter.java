package com.liyz.auth.common.base.filter.dubbo;

import com.liyz.auth.common.base.constant.CommonConstant;
import com.liyz.auth.common.base.trace.TraceContext;
import com.liyz.auth.common.base.trace.TraceInfo;
import com.liyz.auth.common.base.trace.util.AspectUtil;
import com.liyz.auth.common.base.util.JsonMapperUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;
import org.springframework.beans.factory.annotation.Value;

import java.util.Objects;

/**
 * 注释:TraceId过滤器
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2020/9/28 16:20
 */
@Slf4j
@Activate(group = {CommonConstants.PROVIDER})
public class TraceInfoProviderFilter implements Filter {

    @Value("${dubbo.trace.filter:false}")
    private boolean dubboFilter;

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        String traceId = RpcContext.getContext().getAttachment(CommonConstant.REQUEST_ID);
        String parentSpanId = RpcContext.getContext().getAttachment(CommonConstant.PARENT_SPAN_ID);
        TraceInfo traceInfo = new TraceInfo();
        if (!dubboFilter && StringUtils.isNotBlank(traceId)) {
            traceInfo.setTraceId(traceId);
            traceInfo.setSpanId(TraceContext.createSpanId());
            traceInfo.setParentSpanId(parentSpanId);
            invocation.getArguments();
            traceInfo.setParams(AspectUtil.getDubboMethodParams(invoker, invocation));
            traceInfo.setMethod(invocation.getMethodName());
            TraceContext.setTraceInfo(traceInfo);
            log.info("traceId : {}, spanId : {}, parentSpanId : {}, method : {}, params : {}", traceInfo.getTraceId(),
                    traceInfo.getSpanId(), traceInfo.getParentSpanId(), traceInfo.getMethod(), traceInfo.getParams());
        }
        Result result = invoker.invoke(invocation);
        if (!dubboFilter && StringUtils.isNotBlank(traceId)) {
            traceInfo.setDuration(System.currentTimeMillis() - traceInfo.getTimestamp());
            if (Objects.isNull(result.getException())) {
                traceInfo.setResult(JsonMapperUtil.toJSONString(result.getValue()));
                log.info("traceId : {}, spanId : {}, parentSpanId : {}, method : {} ; duration : {} ms ; response result : {}",
                        traceInfo.getTraceId(), traceInfo.getSpanId(), traceInfo.getParentSpanId(), traceInfo.getMethod(),
                        traceInfo.getDuration(), traceInfo.getResult());
            } else {
                traceInfo.setIsNormal(Boolean.FALSE);
                log.error("traceId : {}, spanId : {}, parentSpanId : {}, method : {} ; duration : {} ms ; response result : {}",
                        traceInfo.getTraceId(), traceInfo.getSpanId(), traceInfo.getParentSpanId(), traceInfo.getMethod(),
                        traceInfo.getDuration(), traceInfo.getResult());
            }
            TraceContext.removeTraceInfo();
        }
        return result;
    }
}
