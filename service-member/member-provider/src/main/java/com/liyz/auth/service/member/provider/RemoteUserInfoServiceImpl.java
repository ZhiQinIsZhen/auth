package com.liyz.auth.service.member.provider;

import com.liyz.auth.common.base.trace.annotation.Logs;
import com.liyz.auth.common.base.util.CommonCloneUtil;
import com.liyz.auth.common.remote.page.Page;
import com.liyz.auth.service.member.bo.UserInfoBO;
import com.liyz.auth.service.member.bo.UserRegisterBO;
import com.liyz.auth.service.member.constant.MemberEnum;
import com.liyz.auth.service.member.model.UserInfoDO;
import com.liyz.auth.service.member.remote.RemoteUserInfoService;
import com.liyz.auth.service.member.service.IUserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/4/13 13:56
 */
@Slf4j
@DubboService
public class RemoteUserInfoServiceImpl implements RemoteUserInfoService {

    @Resource
    private IUserInfoService userInfoService;

    @Logs
    @Override
    public UserInfoBO register(UserRegisterBO userRegisterBO) {
        return null;
    }

    @Logs
    @Override
    public UserInfoBO getByUserId(Long userId) {
        return CommonCloneUtil.objectClone(userInfoService.getByUserId(userId), UserInfoBO.class);
    }

    @Logs
    @Override
    public Page<UserInfoBO> pageList(Integer page, Integer size) {
        com.baomidou.mybatisplus.extension.plugins.pagination.Page<UserInfoDO> doPage = userInfoService.page(page, size);
        Page<UserInfoDO> userInfoDOPage = new Page<>(doPage.getRecords(), doPage.getTotal(), (int) doPage.getPages(),
                (int) doPage.getCurrent(), (int) doPage.getSize(), doPage.hasNext());
        return CommonCloneUtil.pageClone(userInfoDOPage, UserInfoBO.class);
    }

    @Logs
    @Override
    public UserInfoBO getByCondition(UserInfoBO userInfoBO) {
        return CommonCloneUtil.objectClone(
                userInfoService.getByCondition(CommonCloneUtil.objectClone(userInfoBO, UserInfoDO.class)), UserInfoBO.class
        );
    }

    @Logs
    @Override
    public Date loginTime(Long userId, String ip, MemberEnum.DeviceEnum deviceEnum) {
        return null;
    }

    @Logs
    @Override
    public Date kickDownLine(Long userId, MemberEnum.DeviceEnum deviceEnum) {
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void testTransactionSynchronization(Long userId) {
        /**
         * test transaction synchronization
         */
        UserInfoDO userInfoDO = userInfoService.getByUserId(userId);
        userInfoService.updateAppTimeByUsername(userInfoDO.getLoginName());
        testTransactionSynchronization();
    }

    @Override
    public void testTransactionSynchronization1(Long userId) {
        /**
         * test transaction synchronization
         */
        testTransactionSynchronization();
    }

    private void testTransactionSynchronization() {
        log.info("isActualTransactionActive:{}", TransactionSynchronizationManager.isActualTransactionActive());
        log.info("isSynchronizationActive:{}", TransactionSynchronizationManager.isSynchronizationActive());
        if (TransactionSynchronizationManager.isActualTransactionActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {

                @Override
                public void beforeCompletion() {
                    log.info("beforeCompletion");
                    TransactionSynchronization.super.beforeCompletion();
                }

                @Override
                public void afterCommit() {
                    log.info("afterCommit");
                    TransactionSynchronization.super.afterCommit();
                }

                @Override
                public void afterCompletion(int status) {
                    log.info("afterCompletion status:{}", status);
                    TransactionSynchronization.super.afterCompletion(status);
                }
            });
        }
    }
}
