package com.liyz.auth.service.staff.provider;

import com.liyz.auth.common.remote.exception.CommonExceptionCodeEnum;
import com.liyz.auth.service.staff.exception.RemoteStaffServiceException;
import com.liyz.auth.service.staff.model.RuleLogDO;
import com.liyz.auth.service.staff.remote.RemoteRuleLogService;
import com.liyz.auth.service.staff.service.IRuleLogService;
import io.seata.core.context.RootContext;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/9/1 15:30
 */
@Slf4j
@DubboService
public class RemoteRuleLogServiceImpl implements RemoteRuleLogService {

    @Resource
    private IRuleLogService ruleLogService;

    @Override
    @GlobalTransactional(timeoutMills = 300000, name = "order-tx")
    public void insert(String username) {
        String xid = RootContext.getXID();
        log.info("xid : {}", xid);
        RuleLogDO ruleLogDO = new RuleLogDO();
        ruleLogDO.setName(username);
        ruleLogDO.setPwd("Aa123456");
        if (StringUtils.isNotBlank(username)) {
            throw new RemoteStaffServiceException(CommonExceptionCodeEnum.ParameterError);
        }
        ruleLogService.save(ruleLogDO);
    }
}
