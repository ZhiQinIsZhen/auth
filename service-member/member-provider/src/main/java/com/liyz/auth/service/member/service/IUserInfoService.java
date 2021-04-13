package com.liyz.auth.service.member.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.liyz.auth.service.member.model.UserInfoDO;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/4/13 15:03
 */
public interface IUserInfoService {

    /**
     * 根据userId获取用户信息
     *
     * @param userId
     * @return
     */
    UserInfoDO getByUserId(Long userId);

    /**
     * 根据条件获取用户信息
     *
     * @param userInfoDO
     * @return
     */
    UserInfoDO getByCondition(UserInfoDO userInfoDO);

    /**
     * 简单分页查询
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    Page<UserInfoDO> page(Integer pageNum, Integer pageSize);
}
