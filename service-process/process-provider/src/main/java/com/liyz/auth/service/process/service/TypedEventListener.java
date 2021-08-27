package com.liyz.auth.service.process.service;

import org.activiti.engine.delegate.event.ActivitiEventListener;
import org.activiti.engine.delegate.event.ActivitiEventType;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/8/27 15:18
 */
public interface TypedEventListener extends ActivitiEventListener {

    /**
     * 指定消费的事件类型列表
     */
    ActivitiEventType[] getType();
}
