package com.liyz.auth.service.member.provider;

import com.liyz.auth.service.member.bo.UserInfoBO;
import com.liyz.auth.service.member.bo.UserRegisterBO;
import com.liyz.auth.service.member.constant.MemberEnum;
import com.liyz.auth.service.member.remote.RemoteUserInfoService;
import com.liyz.auth.common.base.util.CommonCloneUtil;
import com.liyz.auth.common.remote.page.Page;
import com.liyz.auth.service.member.model.UserInfoDO;
import com.liyz.auth.service.member.service.IUserInfoService;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/4/13 13:56
 */
@DubboService(version = "1.0.0")
public class RemoteUserInfoServiceImpl implements RemoteUserInfoService {

    @Resource
    private IUserInfoService userInfoService;

    @Override
    public UserInfoBO register(UserRegisterBO userRegisterBO) {
        return null;
    }

    @Override
    public UserInfoBO getByUserId(Long userId) {
        return CommonCloneUtil.objectClone(userInfoService.getByUserId(userId), UserInfoBO.class);
    }

    @Override
    public Page<UserInfoBO> pageList(Integer page, Integer size) {
        com.baomidou.mybatisplus.extension.plugins.pagination.Page<UserInfoDO> doPage = userInfoService.page(page, size);
        Page<UserInfoDO> userInfoDOPage = new Page<>(doPage.getRecords(), doPage.getTotal(), (int) doPage.getPages(),
                (int) doPage.getCurrent(), (int) doPage.getSize(), doPage.hasNext());
        return CommonCloneUtil.pageClone(userInfoDOPage, UserInfoBO.class);
    }

    @Override
    public UserInfoBO getByCondition(UserInfoBO userInfoBO) {
        return CommonCloneUtil.objectClone(
                userInfoService.getByCondition(CommonCloneUtil.objectClone(userInfoBO, UserInfoDO.class)), UserInfoBO.class
        );
    }

    @Override
    public Date loginTime(Long userId, String ip, MemberEnum.DeviceEnum deviceEnum) {
        return null;
    }

    @Override
    public Date kickDownLine(Long userId, MemberEnum.DeviceEnum deviceEnum) {
        return null;
    }
}
