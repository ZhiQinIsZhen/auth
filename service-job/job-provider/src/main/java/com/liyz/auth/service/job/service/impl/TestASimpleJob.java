package com.liyz.auth.service.job.service.impl;

import com.liyz.auth.common.job.annotation.ElasticJob;
import com.liyz.auth.common.util.JsonMapperUtil;
import com.liyz.auth.service.staff.remote.RemoteCustomerService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.shardingsphere.elasticjob.api.ShardingContext;
import org.apache.shardingsphere.elasticjob.simple.job.SimpleJob;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/8/31 14:43
 */
@Slf4j
@ElasticJob(cron = "0/10 * * * * ?", shardingTotalCount = 1, shardingItemParameters = "0=Beijing")
@Component
public class TestASimpleJob implements SimpleJob {

    @DubboReference
    private RemoteCustomerService remoteCustomerService;

    @Override
    public void execute(ShardingContext shardingContext) {
        log.info("Item: {} | Time: {} | Thread: {} | {}",
                shardingContext.getShardingItem(),
                new SimpleDateFormat("HH:mm:ss").format(new Date()),
                Thread.currentThread().getId(),
                "SIMPLE");
        log.info("customer:{}", JsonMapperUtil.toJSONString(remoteCustomerService.getByUsername("25988654730")));
    }
}
