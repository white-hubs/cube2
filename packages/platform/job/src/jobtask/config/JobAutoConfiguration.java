package com.guhecloud.platform.job.jobtask.config;

import com.guhecloud.platform.job.jobtask.bootstrap.JobScanner;
import com.guhecloud.platform.job.xxl.config.XxlJobProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * XXL-Job 自动配置类
 * <p>
 * 功能：
 * - 自动创建 XxlJobSpringExecutor 执行器实例
 * - 自动注入 XxlJobProperties 配置
 * - 自动注册任务扫描器 MyScheduledJobRegistrar
 * <p>
 */
@Configuration
@EnableConfigurationProperties(XxlJobProperties.class)
@ConditionalOnProperty(prefix = "job.scheduler", name = "type")
public class JobAutoConfiguration {

    /**
     * 创建任务扫描器（不注册为 Bean，仅供内部使用）
     */
    @Bean
    @ConditionalOnMissingBean
    public JobScanner jobRegistrationScanner(ApplicationContext applicationContext) {
        return new JobScanner(applicationContext);
    }
}
