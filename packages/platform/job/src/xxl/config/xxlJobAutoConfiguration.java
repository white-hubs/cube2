package com.guhecloud.platform.job.xxl.config;

import com.guhecloud.platform.job.jobtask.config.JobAutoConfiguration;
import com.guhecloud.platform.job.jobtask.core.JobScheduler;
import com.guhecloud.platform.job.xxl.impl.XxlJobScheduler;
import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * XXL-Job 自动配置类
 */
@Configuration
@AutoConfigureBefore(JobAutoConfiguration.class)
@EnableConfigurationProperties(XxlJobProperties.class)
@ConditionalOnProperty(prefix = "job.scheduler", name = "type")
public class xxlJobAutoConfiguration {

    /**
     * 创建 XXL-Job 执行器实例
     * 该实例负责与调度中心通信、注册执行器、接收任务等
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(name = "job.scheduler.type", havingValue = "xxl-job", matchIfMissing = true)
    public XxlJobSpringExecutor xxlJobExecutor(XxlJobProperties properties) {
        var executor = new XxlJobSpringExecutor();

        // 设置调度中心地址
        executor.setAdminAddresses(properties.getAdmin().getAddresses());

        // 设置执行器名称（必须与控制台一致）
        executor.setAppname(properties.getExecutor().getAppname());

        // 设置调度地址
        executor.setAddress(properties.getExecutor().getAddress());

        // 设置执行器 IP（可选）
        executor.setIp(properties.getExecutor().getIp());

        // 设置内嵌服务端口（必须唯一）
        executor.setPort(properties.getExecutor().getPort());

        // 设置访问令牌（安全校验）
        executor.setAccessToken(properties.getAccessToken());

        // 设置任务日志路径
        executor.setLogPath(properties.getExecutor().getLogpath());

        // 设置日志保留天数
        executor.setLogRetentionDays(properties.getExecutor().getLogretentiondays());

        return executor;
    }

    /**
     * 条件化创建 XXL-JOB 调度器
     */
    @Bean
    @ConditionalOnProperty(name = "job.scheduler.type", havingValue = "xxl-job")
    public JobScheduler xxlJobScheduler() {
        return new XxlJobScheduler();
    }
}
