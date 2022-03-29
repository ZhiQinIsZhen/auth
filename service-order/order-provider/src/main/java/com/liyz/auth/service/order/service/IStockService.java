package com.liyz.auth.service.order.service;

import com.liyz.auth.service.order.model.StockDO;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2022/3/3 10:39
 */
public interface IStockService {

    int getByOrderCode(String orderCode);

    void insert(StockDO stockDO);

    int updateByOrderCode(StockDO stockDO);
}
