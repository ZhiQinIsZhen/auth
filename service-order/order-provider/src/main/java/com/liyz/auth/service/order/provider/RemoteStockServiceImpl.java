package com.liyz.auth.service.order.provider;

import com.liyz.auth.common.base.util.CommonCloneUtil;
import com.liyz.auth.service.order.bo.StockBO;
import com.liyz.auth.service.order.model.StockDO;
import com.liyz.auth.service.order.remote.RemoteStockService;
import com.liyz.auth.service.order.service.IStockService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2022/3/4 15:00
 */
@Slf4j
@DubboService
public class RemoteStockServiceImpl implements RemoteStockService {

    @Resource
    private IStockService stockService;

    @Override
    @Transactional
    public void subAmount(StockBO stockBO) {
        stockService.updateByOrderCode(CommonCloneUtil.objectClone(stockBO, StockDO.class));
    }
}
