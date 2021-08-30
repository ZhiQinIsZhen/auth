package com.liyz.auth.security.server.provider;

import com.liyz.auth.common.base.util.CommonCloneUtil;
import com.liyz.auth.security.base.remote.RemoteGrantedAuthorityService;
import com.liyz.auth.security.base.user.GrantedAuthority;
import com.liyz.auth.security.remote.bo.GrantedAuthorityBO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;

import java.util.List;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/4/13 10:12
 */
@DubboService
public class RemoteGrantedAuthorityServiceImpl implements RemoteGrantedAuthorityService {

    @DubboReference(timeout = 5000, group = "staff")
    com.liyz.auth.security.remote.RemoteGrantedAuthorityService staffGrantedAuthorityService;

    @Override
    public List<GrantedAuthority> getByRoleId(Integer roleId) {
        List<GrantedAuthorityBO> boList = staffGrantedAuthorityService.getByRoleId(roleId);
        return CommonCloneUtil.ListClone(boList, GrantedAuthority.class);
    }

    @Override
    public List<GrantedAuthority> getByRoleIds(List<Integer> roleIds) {
        List<GrantedAuthorityBO> boList = staffGrantedAuthorityService.getByRoleIds(roleIds);
        return CommonCloneUtil.ListClone(boList, GrantedAuthority.class);
    }
}
