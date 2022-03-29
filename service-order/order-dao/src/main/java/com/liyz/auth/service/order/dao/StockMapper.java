package com.liyz.auth.service.order.dao;

import com.liyz.auth.service.order.model.StockDO;
import org.apache.ibatis.annotations.Param;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2022/3/3 10:29
 */
public interface StockMapper {

    int getByOrderCode(@Param("orderCode") String orderCode);

    void insert(StockDO stockDO);

    int updateByOrderCode(StockDO stockDO);
}
