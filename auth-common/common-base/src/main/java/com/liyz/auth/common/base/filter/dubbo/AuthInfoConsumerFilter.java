package com.liyz.auth.common.base.filter.dubbo;

import com.liyz.auth.common.base.constant.CommonConstant;
import com.liyz.auth.common.base.util.AuthContext;
import com.liyz.auth.common.base.util.AuthUser;
import com.liyz.auth.common.base.util.JsonMapperUtil;
import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;

import java.util.Objects;

/**
 * 注释:认证信息过滤器
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/11/18 14:31
 */
@Activate(group = {CommonConstants.CONSUMER})
public class AuthInfoConsumerFilter implements Filter {

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        AuthUser authUser = AuthContext.getAuthUser();
        if (Objects.nonNull(authUser)) {
            RpcContext.getContext().setAttachment(CommonConstant.AUTH_INFO, JsonMapperUtil.toJSONString(authUser));
        }
        Result result = invoker.invoke(invocation);
        return result;
    }
}
