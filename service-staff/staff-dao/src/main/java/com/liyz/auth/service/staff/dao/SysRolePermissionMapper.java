package com.liyz.auth.service.staff.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.liyz.auth.service.staff.model.SysPermissionDO;
import com.liyz.auth.service.staff.model.SysRolePermissionDO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/5/7 10:47
 */
public interface SysRolePermissionMapper extends BaseMapper<SysRolePermissionDO> {

    @Select("SELECT b.permission_url permissionUrl,b.method\n" +
            "from sys_role_permission a\n" +
            "left join sys_permission b\n" +
            "on a.permission_id = b.permission_id\n" +
            "where role_id = #{roleId}\n" +
            "and a.is_inactive = 0\n" +
            "and b.is_inactive = 0")
    List<SysPermissionDO> getByRoleId(@Param("roleId") Integer roleId);
}
