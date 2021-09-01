package com.liyz.auth.service.order.service;

/**
 * 注释:订单状态迁移事件
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/5/11 13:46
 */
public interface OrderStateEvent {

    /**
     * 订单状态事件
     */
    String getEventType();
    /**
     * 订单ID
     */
    String getOrderId();
    /**
     * 如果orderState不为空，则代表只有订单是当前状态才进行迁移
     */
    default String orderState() {
        return null;
    }
    /**
     * 是否要新创建订单
     */
    boolean newCreate();
}
