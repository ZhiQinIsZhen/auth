package com.liyz.auth.service.staff.provider;


import com.google.common.collect.Lists;
import com.liyz.auth.common.base.util.CommonCloneUtil;
import com.liyz.auth.security.remote.RemoteGrantedAuthorityService;
import com.liyz.auth.security.remote.bo.GrantedAuthorityBO;
import com.liyz.auth.service.staff.service.ISysRolePermissionService;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/5/7 11:21
 */
@DubboService(version = "1.0.0", group = "staff")
public class RemoteRolePermissionImpl implements RemoteGrantedAuthorityService {

    @Resource
    private ISysRolePermissionService sysRolePermissionService;

    @Override
    public List<GrantedAuthorityBO> getByRoleId(Integer roleId) {
        if (Objects.isNull(roleId)) {
            return Lists.newArrayList();
        }
        return CommonCloneUtil.ListClone(sysRolePermissionService.getByRoleId(roleId), GrantedAuthorityBO.class);
    }
}
