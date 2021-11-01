package com.liyz.auth.security.base.remote;

import com.liyz.auth.security.base.user.GrantedAuthority;

import java.util.List;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/4/12 17:18
 */
public interface RemoteGrantedAuthorityService {

    List<GrantedAuthority> getByRoleId(Integer roleId, String group);

    List<GrantedAuthority> getByRoleIds(List<Integer> roleIds, String group);
}
