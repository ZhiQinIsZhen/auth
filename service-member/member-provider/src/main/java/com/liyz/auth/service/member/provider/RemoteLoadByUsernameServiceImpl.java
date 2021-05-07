package com.liyz.auth.service.member.provider;

import com.liyz.auth.common.base.util.CommonCloneUtil;
import com.liyz.auth.common.remote.exception.CommonExceptionCodeEnum;
import com.liyz.auth.common.remote.exception.RemoteServiceException;
import com.liyz.auth.security.remote.RemoteLoadByUsernameService;
import com.liyz.auth.security.remote.bo.AuthUserBO;
import com.liyz.auth.service.member.model.UserInfoDO;
import com.liyz.auth.service.member.service.IUserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/4/13 14:58
 */
@Slf4j
@DubboService(version = "1.0.0", group = "member")
public class RemoteLoadByUsernameServiceImpl implements RemoteLoadByUsernameService {

    @Resource
    private IUserInfoService userInfoService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AuthUserBO login(String username) {
        if (StringUtils.isBlank(username)) {
            return null;
        }
        if (!userInfoService.updateByUsername(username)) {
            throw new RemoteServiceException(CommonExceptionCodeEnum.ParameterError);
        }
        UserInfoDO userInfoDO = new UserInfoDO();
        userInfoDO.setLoginName(username);
        UserInfoDO infoDO = userInfoService.getByCondition(userInfoDO);
        return CommonCloneUtil.objectClone(infoDO, AuthUserBO.class);
    }

    @Override
    public AuthUserBO loadByUsername(String username) {
        if (StringUtils.isBlank(username)) {
            return null;
        }
        UserInfoDO userInfoDO = new UserInfoDO();
        userInfoDO.setLoginName(username);
        UserInfoDO infoDO = userInfoService.getByCondition(userInfoDO);
        return CommonCloneUtil.objectClone(infoDO, AuthUserBO.class);
    }
}
