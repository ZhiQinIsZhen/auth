package com.liyz.auth.service.member.event;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/11/24 14:50
 */
@Getter
@Setter
public class TransactionEvent extends ApplicationEvent {

    public TransactionEvent(Object source) {
        super(source);
    }
}
