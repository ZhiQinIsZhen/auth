package com.liyz.auth.service.member.listener;

import com.liyz.auth.service.member.event.TransactionEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * 注释:事务监听器
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/11/24 14:48
 */
@Slf4j
@Component
public class TransactionListener {

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handler(TransactionEvent event) {
        log.info("event start, after commit");
    }
}
