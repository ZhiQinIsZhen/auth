package com.liyz.auth.service.order.context;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/5/11 15:55
 */
@Getter
@Setter
public class OrderInfo implements Serializable {
    private static final long serialVersionUID = -5212851816683043877L;

    private String userId;

    private String orderId;

    private String orderState;
}
