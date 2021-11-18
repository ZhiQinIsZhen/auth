package com.liyz.auth.service.member.provider;

import com.liyz.auth.common.base.trace.annotation.Logs;
import com.liyz.auth.common.base.util.AuthContext;
import com.liyz.auth.common.base.util.CommonCloneUtil;
import com.liyz.auth.common.base.util.JsonMapperUtil;
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
@DubboService(group = "member")
public class RemoteLoadByUsernameServiceImpl implements RemoteLoadByUsernameService {

    @Resource
    private IUserInfoService userInfoService;

    @Logs
    @Override
    @Transactional(rollbackFor = Exception.class)
    public AuthUserBO login(String username) {
        if (StringUtils.isBlank(username)) {
            return null;
        }
        boolean count = userInfoService.updateByUsername(username);
        if (!count) {
            log.error("userName : {}, login fail");
        }
        UserInfoDO userInfoDO = new UserInfoDO();
        userInfoDO.setLoginName(username);
        UserInfoDO infoDO = userInfoService.getByCondition(userInfoDO);
        return CommonCloneUtil.objectClone(infoDO, AuthUserBO.class);
    }

    @Logs
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
