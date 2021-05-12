package com.liyz.auth.service.oreder.context;

import com.liyz.auth.service.oreder.service.FsmOrder;
import lombok.Getter;
import lombok.Setter;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/5/11 13:40
 */
@Getter
@Setter
public class StateContext<C> {

    /**
     * 订单操作事件
     */
    private OrderStateEvent orderStateEvent;
    /**
     * 状态机需要的订单基本信息
     */
    private FsmOrder fsmOrder;

    /**
     * 业务可定义的上下文泛型对象
     */
    private C context;

    public StateContext() {}

    public StateContext(OrderStateEvent orderStateEvent, FsmOrder fsmOrder) {
        this.orderStateEvent = orderStateEvent;
        this.fsmOrder = fsmOrder;
    }
}
