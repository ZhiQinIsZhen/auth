package com.liyz.auth.service.staff.provider;

import com.alibaba.fastjson.JSON;
import com.liyz.auth.common.base.util.CommonCloneUtil;
import com.liyz.auth.common.remote.exception.CommonExceptionCodeEnum;
import com.liyz.auth.common.remote.exception.RemoteServiceException;
import com.liyz.auth.security.remote.RemoteLoadByUsernameService;
import com.liyz.auth.security.remote.bo.AuthUserBO;
import com.liyz.auth.service.staff.model.CustomerDO;
import com.liyz.auth.service.staff.model.RuleLogDO;
import com.liyz.auth.service.staff.service.ICustomerService;
import com.liyz.auth.service.staff.service.IRuleLogService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/5/7 11:27
 */
@Slf4j
@DubboService(version = "1.0.0", group = "staff")
public class RemoteLoadByUsernameServiceImpl implements RemoteLoadByUsernameService {

    @Resource
    private ICustomerService customerService;
    @Resource
    private IRuleLogService ruleLogService;

    @Override
    public AuthUserBO login(String username) {
        if (StringUtils.isBlank(username)) {
            return null;
        }
        if (!customerService.updateByUsername(username)) {
            throw new RemoteServiceException(CommonExceptionCodeEnum.ParameterError);
        }
        RuleLogDO ruleLogDO = new RuleLogDO();
        ruleLogDO.setName(username);
        ruleLogDO.setPwd("Aa123456");
        ruleLogService.save(ruleLogDO);
        return loadByUsername(username);
    }

    @Override
    public AuthUserBO loadByUsername(String username) {
        if (StringUtils.isBlank(username)) {
            return null;
        }
        CustomerDO customerDO = new CustomerDO();
        customerDO.setCustomerName(username);
        customerDO.setIsInactive(0);
        customerDO = customerService.getOne(customerDO);
        AuthUserBO authUserBO = CommonCloneUtil.objectClone(customerDO, AuthUserBO.class);
        if (Objects.nonNull(customerDO)) {
            authUserBO.setUserName(customerDO.getCustomerName());
            authUserBO.setLoginName(customerDO.getCustomerName());
            authUserBO.setLoginPwd(customerDO.getPassword());
            authUserBO.setUserId(customerDO.getCustomerId());
        }
        log.info("ruleLog json : {}", JSON.toJSONString(ruleLogService.getById(11)));
        return authUserBO;
    }
}
