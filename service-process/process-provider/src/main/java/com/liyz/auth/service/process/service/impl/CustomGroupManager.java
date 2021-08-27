package com.liyz.auth.service.process.service.impl;

import org.activiti.engine.identity.Group;
import org.activiti.engine.impl.persistence.entity.GroupEntityManager;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/8/27 15:14
 */
@Service
public class CustomGroupManager extends GroupEntityManager {

    @Override
    public List<Group> findGroupsByUser(String userId) {
        return Collections.emptyList();
    }
}
