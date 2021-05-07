package com.liyz.auth.security.remote;

import com.liyz.auth.security.remote.bo.GrantedAuthorityBO;

import java.util.List;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/4/12 17:18
 */
public interface RemoteGrantedAuthorityService {

    List<GrantedAuthorityBO> getByRoleId(Integer roleId);
}
