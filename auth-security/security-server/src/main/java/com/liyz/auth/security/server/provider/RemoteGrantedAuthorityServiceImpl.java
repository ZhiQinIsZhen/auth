package com.liyz.auth.security.server.provider;

import com.google.common.collect.Lists;
import com.liyz.auth.common.base.util.CommonCloneUtil;
import com.liyz.auth.security.base.constant.SecurityConstant;
import com.liyz.auth.security.base.remote.RemoteGrantedAuthorityService;
import com.liyz.auth.common.base.util.GrantedAuthority;
import com.liyz.auth.security.remote.bo.GrantedAuthorityBO;
import com.liyz.auth.security.server.util.DubboGenericServiceUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/4/13 10:12
 */
@Slf4j
@DubboService
public class RemoteGrantedAuthorityServiceImpl implements RemoteGrantedAuthorityService {

    private static final Object lock = new Object();

    @Override
    public List<GrantedAuthority> getByRoleId(Integer roleId, String group) {
        List<GrantedAuthorityBO> boList = getAuthority("getByRoleId", roleId, group);
        return CommonCloneUtil.ListClone(boList, GrantedAuthority.class);
    }

    @Override
    public List<GrantedAuthority> getByRoleIds(List<Integer> roleIds, String group) {
        List<GrantedAuthorityBO> boList = getAuthority("getByRoleIds", roleIds, group);
        return CommonCloneUtil.ListClone(boList, GrantedAuthority.class);
    }

    /**
     * 获取权限
     *
     * @param methodName
     * @param o
     * @return
     */
    private List<GrantedAuthorityBO> getAuthority(String methodName, Object o, String group) {
        String[] parameterTypes;
        if (o instanceof List) {
            parameterTypes = new String[] {List.class.getName()};
        } else {
            parameterTypes = new String[] {Integer.class.getName()};
        }
        List<Map<String, Object>> mapList = (List<Map<String, Object>>) DubboGenericServiceUtil
                .getByClassName(
                        com.liyz.auth.security.remote.RemoteGrantedAuthorityService.class,
                        SecurityConstant.DEFAULT_VERSION,
                        group
                )
                .$invoke(methodName, parameterTypes, new Object[] {o});
        List<GrantedAuthorityBO> list = Lists.newArrayList();
        if (!CollectionUtils.isEmpty(mapList)) {
            mapList.stream().forEach(item -> {
                try {
                    GrantedAuthorityBO authorityBO = CommonCloneUtil.MapToBean(item, GrantedAuthorityBO.class);
                    list.add(authorityBO);
                } catch (Exception e) {
                    log.error("权限列表类型转换 error", e);
                }
            });
        }
        return list;
    }
}
