package com.liyz.auth.service.oreder.service.impl;

import com.liyz.auth.service.oreder.annotation.OrderProcessor;
import com.liyz.auth.service.oreder.constant.OrderEventEnum;
import com.liyz.auth.service.oreder.constant.OrderStateEnum;
import com.liyz.auth.service.oreder.context.CreateOrderContext;
import com.liyz.auth.service.oreder.context.OrderInfo;
import com.liyz.auth.service.oreder.context.ServiceResult;
import com.liyz.auth.service.oreder.context.StateContext;
import com.liyz.auth.service.oreder.service.Checkable;
import com.liyz.auth.service.oreder.service.Checker;
import com.liyz.auth.service.oreder.service.StateProcessor;
import com.liyz.auth.service.oreder.service.abs.AbstractStateProcessor;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 注释:创建订单状态对应的状态处理器
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/5/11 13:42
 */
@Slf4j
@OrderProcessor(state = OrderStateEnum.INIT, event = OrderEventEnum.CREATE, bizCode = {"CHEAP", "POPULAR"}, sceneId = "H5")
public class OrderCreatedProcessor extends AbstractStateProcessor<String, CreateOrderContext> {

    @Resource
    private CreateParamChecker createParamChecker;
    @Resource
    private UserChecker userChecker;
    @Resource
    private UnfinshChecker unfinshChecker;

    @Override
    public Checkable getCheckable(StateContext<CreateOrderContext> context) {
        return new Checkable() {
            @Override
            public List<Checker> getParamChecker() {
                return Arrays.asList(createParamChecker);
            }

            @Override
            public List<Checker> getSyncChecker() {
                return Collections.EMPTY_LIST;
            }

            @Override
            public List<Checker> getAsyncChecker() {
                return Arrays.asList(userChecker, unfinshChecker);
            }
        };
    }

    @Override
    public String getNextState(StateContext<CreateOrderContext> context) {
        return OrderStateEnum.NEW.name();
    }

    @Override
    public ServiceResult<String> action(String nextState, StateContext<CreateOrderContext> context) {
        return null;
    }

    @Override
    public ServiceResult<String> save(String nextState, StateContext<CreateOrderContext> context) {
        OrderInfo orderInfo = context.getContext().getOrderInfo();
        // 更新状态
        orderInfo.setOrderState(nextState);
        // 持久化
//        this.updateOrderInfo(orderInfo);
        log.info("save BUSINESS order success, userId:{}, orderId:{}", orderInfo.getUserId(), orderInfo.getOrderId());
        return new ServiceResult<>(orderInfo.getOrderId(), "business下单成功");
    }

    @Override
    public void after(StateContext<CreateOrderContext> context) {

    }
}
