package com.liyz.auth.service.staff.remote;

import com.liyz.auth.service.staff.bo.CustomerBO;

import javax.validation.constraints.NotBlank;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2020/8/17 14:01
 */
public interface RemoteCustomerService {

    CustomerBO getByUsername(@NotBlank String username);
}
