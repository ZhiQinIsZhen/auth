package com.liyz.auth.service.order.remote;

import com.liyz.auth.service.order.bo.StockBO;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2022/3/4 14:59
 */
public interface RemoteStockService {

    void subAmount(StockBO stockBO);
}
