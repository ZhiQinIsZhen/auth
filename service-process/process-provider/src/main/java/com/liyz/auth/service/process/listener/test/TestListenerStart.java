package com.liyz.auth.service.process.listener.test;

import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.springframework.stereotype.Service;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/8/27 17:50
 */
@Slf4j
@Service
public class TestListenerStart implements ExecutionListener {

    @Override
    public void notify(DelegateExecution delegateExecution) throws Exception {
        log.info("服务调用开始 process : {}", delegateExecution.getProcessInstanceId());
    }
}
