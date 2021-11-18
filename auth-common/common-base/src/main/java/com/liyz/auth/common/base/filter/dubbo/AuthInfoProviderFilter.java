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
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/11/18 15:06
 */
@Activate(group = {CommonConstants.PROVIDER})
public class AuthInfoProviderFilter implements Filter {

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        boolean needRemove = false;
        AuthUser authUser = AuthContext.getAuthUser();
        if (Objects.isNull(authUser) && RpcContext.getContext().getAttachments().containsKey(CommonConstant.AUTH_INFO)) {
            authUser = JsonMapperUtil.readValue(RpcContext.getContext().getAttachment(CommonConstant.AUTH_INFO), AuthUser.class);
            if (Objects.nonNull(authUser)) {
                AuthContext.setAuthUser(authUser);
                needRemove = true;
            }
        }
        Result result = invoker.invoke(invocation);
        if (needRemove) {
            AuthContext.remove();;
        }
        return result;
    }
}
