package com.liyz.auth.service.staff.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liyz.auth.common.util.DateUtil;
import com.liyz.auth.service.staff.dao.CustomerMapper;
import com.liyz.auth.service.staff.model.CustomerDO;
import com.liyz.auth.service.staff.service.ICustomerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/5/7 11:07
 */
@Service
public class CustomerServiceImpl extends ServiceImpl<CustomerMapper, CustomerDO> implements ICustomerService {

    @Override
    public CustomerDO getOne(CustomerDO customerDO) {
        return super.getOne(Wrappers.lambdaQuery(customerDO));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateByUsername(String username) {
        return super.update(Wrappers.<CustomerDO>lambdaUpdate()
                .set(CustomerDO::getWebTokenTime, DateUtil.currentDate())
                .eq(CustomerDO::getCustomerName, username)
        );
    }
}
