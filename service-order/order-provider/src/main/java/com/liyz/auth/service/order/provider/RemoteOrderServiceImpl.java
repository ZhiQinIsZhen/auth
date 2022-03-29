package com.liyz.auth.service.order.provider;

import com.liyz.auth.common.base.trace.annotation.Logs;
import com.liyz.auth.service.order.model.OrderDO;
import com.liyz.auth.service.order.model.StockDO;
import com.liyz.auth.service.order.remote.RemoteOrderService;
import com.liyz.auth.service.order.service.IOrderService;
import com.liyz.auth.service.order.service.IStockService;
import com.liyz.auth.service.staff.remote.RemoteRuleLogService;
import io.seata.core.context.RootContext;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/9/1 15:25
 */
@Slf4j
@DubboService
public class RemoteOrderServiceImpl implements RemoteOrderService {

    @Resource
    private IOrderService orderService;
    @Resource
    private IStockService stockService;
    @DubboReference(timeout = 500000)
    private RemoteRuleLogService remoteRuleLogService;

    @Override
    @Logs
    @GlobalTransactional(timeoutMills = 300000, name = "order-tx")
    public void insert(String orderCode) {
        String xid = RootContext.getXID();
        log.info("xid : {}", xid);
//        orderService.getById(32);
        OrderDO orderDO = new OrderDO();
        orderDO.setOrderCode(orderCode);
        orderService.save(orderDO);
        StockDO stockDO = new StockDO();
        stockDO.setOrderCode(orderCode);
        if (stockService.getByOrderCode(orderCode) == 0) {
            stockDO.setAmount(100);
            stockService.insert(stockDO);
        } else {
            stockDO.setAmount(1);
            stockService.updateByOrderCode(stockDO);
        }
        remoteRuleLogService.insert(orderCode);
        int a = 100/0;
        log.info("orderCode:{}", orderCode);
    }
}
