package com.liyz.auth.common.base.filter.dubbo;

import com.liyz.auth.common.base.constant.CommonConstant;
import com.liyz.auth.common.base.trace.TraceContext;
import com.liyz.auth.common.base.trace.TraceInfo;
import com.liyz.auth.common.base.trace.util.AspectUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;

import java.util.Objects;

/**
 * 注释:RequestId过滤器
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2020/9/28 16:20
 */
@Slf4j
@Activate(group = {CommonConstants.CONSUMER})
public class TraceInfoConsumerFilter implements Filter {

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        TraceInfo traceInfo = TraceContext.getTraceInfo();
        if (Objects.nonNull(traceInfo)) {
            String traceId = TraceContext.getTraceInfo().getTraceId();
            if (StringUtils.isNotBlank(traceId)) {
                RpcContext.getContext().setAttachment(CommonConstant.REQUEST_ID, traceId);
            }
            String parentSpanId = TraceContext.getTraceInfo().getSpanId();
            if (StringUtils.isNotBlank(parentSpanId)) {
                RpcContext.getContext().setAttachment(CommonConstant.PARENT_SPAN_ID, parentSpanId);
            }
        }
        Result result = invoker.invoke(invocation);
        return result;
    }
}
