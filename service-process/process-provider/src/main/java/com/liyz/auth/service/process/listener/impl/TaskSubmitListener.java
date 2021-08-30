package com.liyz.auth.service.process.listener.impl;

import com.liyz.auth.service.process.listener.TypedEventListener;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.delegate.event.ActivitiEventType;
import org.activiti.engine.delegate.event.impl.ActivitiEntityWithVariablesEventImpl;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.springframework.stereotype.Service;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/8/30 9:54
 */
@Slf4j
@Service
public class TaskSubmitListener implements TypedEventListener {

    @Override
    public ActivitiEventType[] getType() {
        return new ActivitiEventType[]{ActivitiEventType.TASK_COMPLETED};
    }

    @Override
    public void onEvent(ActivitiEvent activitiEvent) {
        if (activitiEvent instanceof ActivitiEntityWithVariablesEventImpl) {
            log.info("###################### task submit  processDefinitionId : {}, processInstanceId : {}, taskId : {}",
                    activitiEvent.getProcessDefinitionId(),
                    activitiEvent.getProcessInstanceId(),
                    ((TaskEntity) ((ActivitiEntityWithVariablesEventImpl) activitiEvent).getEntity()).getId());
        } else {
            log.info("###################### task submit  processDefinitionId : {}, processInstanceId : {}",
                    activitiEvent.getProcessDefinitionId(),
                    activitiEvent.getProcessInstanceId());
        }
    }

    @Override
    public boolean isFailOnException() {
        return false;
    }
}
