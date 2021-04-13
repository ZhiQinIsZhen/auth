package com.liyz.auth.security.server.impl;

import com.liyz.auth.security.base.remote.RemoteGrantedAuthorityService;
import com.liyz.auth.security.base.user.GrantedAuthority;
import org.apache.dubbo.config.annotation.DubboService;

import java.util.List;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/4/13 10:12
 */
@DubboService(version = "1.0.0")
public class RemoteGrantedAuthorityServiceImpl implements RemoteGrantedAuthorityService {

    @Override
    public List<GrantedAuthority> getByRoleId(Integer roleId) {
        return null;
    }
}
