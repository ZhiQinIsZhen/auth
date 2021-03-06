package com.liyz.auth.service.staff.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.liyz.auth.service.staff.model.CustomerDO;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/5/7 11:02
 */
public interface ICustomerService extends IService<CustomerDO> {

    CustomerDO getOne(CustomerDO customerDO);

    boolean updateByUsername(String username);
}
