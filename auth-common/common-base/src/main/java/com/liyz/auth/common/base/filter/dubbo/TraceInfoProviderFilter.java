package com.liyz.auth.common.base.filter.dubbo;

import com.liyz.auth.common.base.constant.CommonConstant;
import com.liyz.auth.common.base.trace.TraceContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;

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

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        String traceId = RpcContext.getContext().getAttachment(CommonConstant.REQUEST_ID);
        if (StringUtils.isNotBlank(traceId)) {
            TraceContext.setTraceId(traceId,
                    new StringBuilder()
                            .append(invocation.getAttachment(CommonConstants.INTERFACE_KEY))
                            .append(CommonConstant.METHOD_SPLIT)
                            .append(invocation.getMethodName())
                            .toString());
        }
        Result result = invoker.invoke(invocation);
        if (StringUtils.isBlank(traceId)) {
            traceId = TraceContext.getTraceId();
        }
        if (StringUtils.isNotBlank(traceId)) {
            result.getAttachments().put(CommonConstant.REQUEST_ID, traceId);
            TraceContext.removeTraceId();
        }
        return result;
    }
}
