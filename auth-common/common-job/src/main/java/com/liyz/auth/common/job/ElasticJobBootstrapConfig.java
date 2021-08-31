package com.liyz.auth.common.job;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import org.apache.shardingsphere.elasticjob.api.ElasticJob;
import org.apache.shardingsphere.elasticjob.api.JobConfiguration;
import org.apache.shardingsphere.elasticjob.lite.api.bootstrap.impl.OneOffJobBootstrap;
import org.apache.shardingsphere.elasticjob.lite.api.bootstrap.impl.ScheduleJobBootstrap;
import org.apache.shardingsphere.elasticjob.lite.spring.boot.job.ElasticJobConfigurationProperties;
import org.apache.shardingsphere.elasticjob.lite.spring.boot.job.ElasticJobProperties;
import org.apache.shardingsphere.elasticjob.reg.base.CoordinatorRegistryCenter;
import org.apache.shardingsphere.elasticjob.tracing.api.TracingConfiguration;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.SingletonBeanRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/8/31 14:52
 */
public class ElasticJobBootstrapConfig implements ApplicationContextAware, BeanPostProcessor {

    private ApplicationContext applicationContext;

    @PostConstruct
    public void createJobBootstrapBeans() {
        ElasticJobProperties elasticJobProperties = this.applicationContext.getBean(ElasticJobProperties.class);
        //扫描目标类
        Map<String, Object> beanMap = this.applicationContext.getBeansWithAnnotation(com.liyz.auth.common.job.annotation.ElasticJob .class);
        if (beanMap != null && beanMap.size() > 0) {
            for (Map.Entry<String, Object> entry : beanMap.entrySet()) {
                String beanName = entry.getKey();
                Class<?> clz = entry.getValue().getClass();
                com.liyz.auth.common.job.annotation.ElasticJob elasticJob =
                        clz.getAnnotation(com.liyz.auth.common.job.annotation.ElasticJob.class);
                ElasticJobConfigurationProperties properties = new ElasticJobConfigurationProperties();
                properties.setElasticJobClass((Class<? extends ElasticJob>) clz);
                properties.setCron(elasticJob.cron());
                properties.setShardingTotalCount(elasticJob.shardingTotalCount());
                properties.setShardingItemParameters(elasticJob.shardingItemParameters());
                properties.setOverwrite(elasticJob.overwrite());
                elasticJobProperties.getJobs().put(beanName, properties);
            }
        }
        SingletonBeanRegistry singletonBeanRegistry = ((ConfigurableApplicationContext)this.applicationContext).getBeanFactory();
        CoordinatorRegistryCenter registryCenter = this.applicationContext.getBean(CoordinatorRegistryCenter.class);
        TracingConfiguration<?> tracingConfig = this.getTracingConfiguration();
        this.constructJobBootstraps(elasticJobProperties, singletonBeanRegistry, registryCenter, tracingConfig);
    }

    private TracingConfiguration<?> getTracingConfiguration() {
        Map<String, TracingConfiguration> tracingConfigurationBeans = this.applicationContext.getBeansOfType(TracingConfiguration.class);
        if (tracingConfigurationBeans.isEmpty()) {
            return null;
        } else if (1 == tracingConfigurationBeans.size()) {
            return (TracingConfiguration)tracingConfigurationBeans.values().iterator().next();
        } else {
            throw new BeanCreationException("More than one [org.apache.shardingsphere.elasticjob.tracing.api.TracingConfiguration] beans found. Consider disabling [org.apache.shardingsphere.elasticjob.tracing.boot.ElasticJobTracingAutoConfiguration].");
        }
    }

    private void constructJobBootstraps(ElasticJobProperties elasticJobProperties, SingletonBeanRegistry singletonBeanRegistry, CoordinatorRegistryCenter registryCenter, TracingConfiguration<?> tracingConfig) {
        Iterator var5 = elasticJobProperties.getJobs().entrySet().iterator();

        while(var5.hasNext()) {
            Map.Entry<String, ElasticJobConfigurationProperties> entry = (Map.Entry)var5.next();
            ElasticJobConfigurationProperties jobConfigurationProperties = entry.getValue();
            Preconditions.checkArgument(null != jobConfigurationProperties.getElasticJobClass() || !Strings.isNullOrEmpty(jobConfigurationProperties.getElasticJobType()), "Please specific [elasticJobClass] or [elasticJobType] under job configuration.");
            Preconditions.checkArgument(null == jobConfigurationProperties.getElasticJobClass() || Strings.isNullOrEmpty(jobConfigurationProperties.getElasticJobType()), "[elasticJobClass] and [elasticJobType] are mutually exclusive.");
            if (null != jobConfigurationProperties.getElasticJobClass()) {
                this.registerClassedJob(entry.getKey(), (entry.getValue()).getJobBootstrapBeanName(), singletonBeanRegistry, registryCenter, tracingConfig, jobConfigurationProperties);
            } else if (!Strings.isNullOrEmpty(jobConfigurationProperties.getElasticJobType())) {
                this.registerTypedJob(entry.getKey(), (entry.getValue()).getJobBootstrapBeanName(), singletonBeanRegistry, registryCenter, tracingConfig, jobConfigurationProperties);
            }
        }

    }

    private void registerClassedJob(String jobName, String jobBootstrapBeanName, SingletonBeanRegistry singletonBeanRegistry, CoordinatorRegistryCenter registryCenter, TracingConfiguration<?> tracingConfig, ElasticJobConfigurationProperties jobConfigurationProperties) {
        JobConfiguration jobConfig = jobConfigurationProperties.toJobConfiguration(jobName);
        Optional var10000 = Optional.ofNullable(tracingConfig);
        Collection var10001 = jobConfig.getExtraConfigurations();
        var10000.ifPresent(var10001::add);
        ElasticJob elasticJob = this.applicationContext.getBean(jobConfigurationProperties.getElasticJobClass());
        if (Strings.isNullOrEmpty(jobConfig.getCron())) {
            Preconditions.checkArgument(!Strings.isNullOrEmpty(jobBootstrapBeanName), "The property [jobBootstrapBeanName] is required for One-off job.");
            singletonBeanRegistry.registerSingleton(jobBootstrapBeanName, new OneOffJobBootstrap(registryCenter, elasticJob, jobConfig));
        } else {
            String beanName = !Strings.isNullOrEmpty(jobBootstrapBeanName) ? jobBootstrapBeanName : jobConfig.getJobName() + "ScheduleJobBootstrap";
            singletonBeanRegistry.registerSingleton(beanName, new ScheduleJobBootstrap(registryCenter, elasticJob, jobConfig));
        }

    }

    private void registerTypedJob(String jobName, String jobBootstrapBeanName, SingletonBeanRegistry singletonBeanRegistry, CoordinatorRegistryCenter registryCenter, TracingConfiguration<?> tracingConfig, ElasticJobConfigurationProperties jobConfigurationProperties) {
        JobConfiguration jobConfig = jobConfigurationProperties.toJobConfiguration(jobName);
        Optional var10000 = Optional.ofNullable(tracingConfig);
        Collection var10001 = jobConfig.getExtraConfigurations();
        var10000.ifPresent(var10001::add);
        if (Strings.isNullOrEmpty(jobConfig.getCron())) {
            Preconditions.checkArgument(!Strings.isNullOrEmpty(jobBootstrapBeanName), "The property [jobBootstrapBeanName] is required for One-off job.");
            singletonBeanRegistry.registerSingleton(jobBootstrapBeanName, new OneOffJobBootstrap(registryCenter, jobConfigurationProperties.getElasticJobType(), jobConfig));
        } else {
            String beanName = !Strings.isNullOrEmpty(jobBootstrapBeanName) ? jobBootstrapBeanName : jobConfig.getJobName() + "ScheduleJobBootstrap";
            singletonBeanRegistry.registerSingleton(beanName, new ScheduleJobBootstrap(registryCenter, jobConfigurationProperties.getElasticJobType(), jobConfig));
        }

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
