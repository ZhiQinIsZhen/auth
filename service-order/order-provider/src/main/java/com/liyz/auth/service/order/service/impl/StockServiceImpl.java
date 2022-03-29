package com.liyz.auth.service.order.service.impl;

import com.liyz.auth.service.order.dao.StockMapper;
import com.liyz.auth.service.order.model.StockDO;
import com.liyz.auth.service.order.service.IStockService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2022/3/3 10:39
 */
@Slf4j
@Service
public class StockServiceImpl implements IStockService {

    @Resource
    private StockMapper stockMapper;

    @Override
    public int getByOrderCode(String orderCode) {
        return stockMapper.getByOrderCode(orderCode);
    }

    @Override
    public void insert(StockDO stockDO) {
        stockMapper.insert(stockDO);
    }

    @Override
    public int updateByOrderCode(StockDO stockDO) {
        return stockMapper.updateByOrderCode(stockDO);
    }
}
