package com.liyz.auth.service.process.config;

import com.liyz.auth.service.process.listener.TypedEventListener;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.*;
import org.activiti.engine.delegate.event.ActivitiEventListener;
import org.activiti.engine.impl.asyncexecutor.DefaultAsyncJobExecutor;
import org.activiti.engine.impl.cfg.IdGenerator;
import org.activiti.engine.impl.history.HistoryLevel;
import org.activiti.spring.ProcessEngineFactoryBean;
import org.activiti.spring.SpringProcessEngineConfiguration;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/8/27 14:39
 */
@Slf4j
@Configuration
public class ProcessConfig implements ApplicationListener<ApplicationStartedEvent> {

    @Bean
    SpringProcessEngineConfiguration processEngineConfiguration(
            HikariDataSource dataSource,
            PlatformTransactionManager transactionManager,
            IdGenerator snowFlakeIdGenerator) {
        SpringProcessEngineConfiguration configuration = new SpringProcessEngineConfiguration();
        // 不自动更新数据库结构，以免集群一个节点升级之后影响另外节点
        configuration.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
        configuration.setDataSource(dataSource);
        configuration.setTransactionManager(transactionManager);
        configuration.setJobExecutorActivate(true);
        configuration.setDbIdentityUsed(false);
//        configuration.setCustomMybatisXMLMappers(new HashSet<>(
//                Arrays.asList("mapper/RunTaskMapper.xml", "mapper/ProcessMapper.xml")));
        configuration.setCustomMybatisXMLMappers(new HashSet<>(
                Arrays.asList("mapper/ProcessMapper.xml")));
        // 开启异步执行功能
        configuration.setAsyncExecutorActivate(true);
        // 设置异步任务线程池大小
        configuration.setAsyncExecutorCorePoolSize(4);
        configuration.setAsyncExecutorMaxPoolSize(10);
        // 显式设置AsyncExecutor（后面很多设置基于DefaultAsyncJobExecutor）
        configuration.setAsyncExecutor(new DefaultAsyncJobExecutor());
        // 获取Job后锁定1分钟
        configuration.setAsyncExecutorAsyncJobLockTimeInMillis(60 * 1000);
        // 每10秒钟检查一次：队列中的Job是否小于缓存Job数
        configuration.setAsyncExecutorDefaultAsyncJobAcquireWaitTime(10 * 1000);
        // 每次缓存10个Job任务
        configuration.setAsyncExecutorMaxAsyncJobsDuePerAcquisition(10);
        // 记录变量在某个节点的值
        configuration.setHistoryLevel(HistoryLevel.FULL);
        // 使用雪花算法来生成ID
        configuration.setIdGenerator(snowFlakeIdGenerator);
        return configuration;
    }

    @Bean
    ProcessEngineFactoryBean processEngineFactoryBean(SpringProcessEngineConfiguration configuration) {
        ProcessEngineFactoryBean factory = new ProcessEngineFactoryBean();
        factory.setProcessEngineConfiguration(configuration);
        return factory;
    }

    @Bean
    RepositoryService repositoryService(ProcessEngine engine) {
        return engine.getRepositoryService();
    }

    @Bean
    RuntimeService runtimeService(ProcessEngine engine) {
        return engine.getRuntimeService();
    }

    @Bean
    TaskService taskService(ProcessEngine engine) {
        return engine.getTaskService();
    }

    @Bean
    ManagementService managementService(ProcessEngine engine) {
        return engine.getManagementService();
    }

    @Bean
    IdentityService identityService(ProcessEngine engine) {
        return engine.getIdentityService();
    }

    @Bean
    FormService formService(ProcessEngine engine) {
        return engine.getFormService();
    }

    @Bean
    HistoryService historyService(ProcessEngine engine) {
        return engine.getHistoryService();
    }

    /**
     *  在应用启动完毕后,注册流程事件监听器
     *  原因:Bean没有初始化完成的时候注册会有循环依赖问题,导致监听器无法注册成功,
     *  所以将这一步移到应用启动完成再做
     *
     * @param applicationStartedEvent
     */
    @Override
    public void onApplicationEvent(ApplicationStartedEvent applicationStartedEvent) {
        RuntimeService runtimeService = applicationStartedEvent.getApplicationContext().getBean("runtimeService", RuntimeService.class);
        Collection<ActivitiEventListener> values = applicationStartedEvent.getApplicationContext().getBeansOfType(ActivitiEventListener.class).values();
        values.forEach(activitiEventListener -> {
            if (activitiEventListener instanceof TypedEventListener) {
                runtimeService.addEventListener(activitiEventListener, ((TypedEventListener) activitiEventListener).getType());
            } else {
                runtimeService.addEventListener(activitiEventListener);
            }
        });
    }
}
