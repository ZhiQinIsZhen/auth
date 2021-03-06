package com.liyz.auth.service.order.service;

/**
 * 注释:状态机引擎所需的订单信息基类信息
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/5/11 15:45
 */
public interface FsmOrder {

    /**
     * 订单ID
     */
    String getOrderId();
    /**
     * 订单状态
     */
    String getOrderState();
    /**
     * 订单的业务属性
     */
    String bizCode();
    /**
     * 订单的场景属性
     */
    String sceneId();
}
