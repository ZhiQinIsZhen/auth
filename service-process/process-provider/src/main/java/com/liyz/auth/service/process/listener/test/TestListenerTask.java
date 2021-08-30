package com.liyz.auth.service.process.listener.test;

import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.springframework.stereotype.Service;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/8/30 10:12
 */
@Slf4j
@Service
public class TestListenerTask implements TaskListener {

    @Override
    public void notify(DelegateTask delegateTask) {
        log.info("任务提交开始 process : {}, taskId : {}", delegateTask.getProcessInstanceId(), delegateTask.getId());
    }
}
