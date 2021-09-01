package com.liyz.auth.service.order.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liyz.auth.service.order.dao.OrderMapper;
import com.liyz.auth.service.order.model.OrderDO;
import com.liyz.auth.service.order.service.IOrderService;
import org.springframework.stereotype.Service;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/9/1 15:24
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, OrderDO> implements IOrderService {
}
