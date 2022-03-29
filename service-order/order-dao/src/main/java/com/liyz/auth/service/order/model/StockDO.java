package com.liyz.auth.service.order.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2022/3/3 10:28
 */
@Getter
@Setter
public class StockDO implements Serializable {
    private static final long serialVersionUID = 6052045642151783436L;

    private String orderCode;

    private Integer amount;
}
