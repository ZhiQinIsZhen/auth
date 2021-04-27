package com.liyz.auth.service.member.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liyz.auth.common.util.DateUtil;
import com.liyz.auth.service.member.dao.UserInfoMapper;
import com.liyz.auth.service.member.model.UserInfoDO;
import com.liyz.auth.service.member.service.IUserInfoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/4/13 13:59
 */
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfoDO> implements IUserInfoService {

    /**
     * 根据userId获取用户信息
     *
     * @param userId
     * @return
     */
    @Override
    public UserInfoDO getByUserId(Long userId) {
        return super.getById(userId);
    }

    /**
     * 根据条件获取用户信息
     *
     * @param userInfoDO
     * @return
     */
    @Override
    public UserInfoDO getByCondition(UserInfoDO userInfoDO) {
        return super.getOne(Wrappers.lambdaQuery(userInfoDO));
    }

    /**
     * 简单分页查询
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public Page<UserInfoDO> page(Integer pageNum, Integer pageSize) {
        Page<UserInfoDO> page = super.page(
                new Page<>(pageNum, pageSize),
                Wrappers.<UserInfoDO>lambdaQuery().orderByAsc(UserInfoDO::getUserId)
        );
        return page;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateByUsername(String username) {
        return super.update(Wrappers.<UserInfoDO>lambdaUpdate()
                .set(UserInfoDO::getWebTokenTime, DateUtil.currentDate())
                .eq(UserInfoDO::getLoginName, username)
        );
    }
}
