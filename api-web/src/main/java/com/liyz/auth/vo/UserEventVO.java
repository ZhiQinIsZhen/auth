package com.liyz.auth.vo;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/11/24 10:29
 */
@Getter
@Setter
public class UserEventVO extends ApplicationEvent {

    private Long userId;

    public UserEventVO(Object source, Long userId) {
        super(source);
        this.userId = userId;
    }
}
