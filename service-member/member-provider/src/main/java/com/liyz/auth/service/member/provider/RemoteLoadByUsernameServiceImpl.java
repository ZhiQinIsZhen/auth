package com.liyz.auth.service.member.provider;

import com.liyz.auth.common.base.util.CommonCloneUtil;
import com.liyz.auth.service.member.model.UserInfoDO;
import com.liyz.auth.service.member.service.IUserInfoService;
import com.liyz.auth.security.remote.RemoteLoadByUsernameService;
import com.liyz.auth.security.remote.bo.AuthUserBO;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/4/13 14:58
 */
@DubboService(version = "1.0.0")
public class RemoteLoadByUsernameServiceImpl implements RemoteLoadByUsernameService {

    @Resource
    private IUserInfoService userInfoService;

    @Override
    public AuthUserBO loadByUsername(String username) {
        if (StringUtils.isBlank(username)) {
            return null;
        }
        UserInfoDO userInfoDO = new UserInfoDO();
        userInfoDO.setLoginName(username);
        return CommonCloneUtil.objectClone(userInfoService.getByCondition(userInfoDO), AuthUserBO.class);
    }
}
