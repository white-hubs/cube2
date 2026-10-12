package com.guhecloud.platform.job.spring.config;

import com.guhecloud.platform.job.jobtask.config.JobAutoConfiguration;
import com.guhecloud.platform.job.jobtask.core.JobScheduler;
import com.guhecloud.platform.job.spring.impl.SpringScheduler;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

/**
 * XXL-Job 自动配置类
 * <p>
 * 功能：
 * - 自动创建 XxlJobSpringExecutor 执行器实例
 * - 自动注入 XxlJobProperties 配置
 * - 自动注册任务扫描器 MyScheduledJobRegistrar
 * <p>
 * 触发条件：
 * 当配置文件中存在 xxl.job.admin-addresses 时才启用
 */
@Configuration
@AutoConfigureBefore(JobAutoConfiguration.class)
@ConditionalOnProperty(prefix = "job.scheduler", name = "type")
public class SpringTaskAutoConfiguration {

    /**
     * 创建任务调度线程池（用于 Spring Task）
     */
    @Bean("taskScheduler")
    @ConditionalOnMissingBean
    @ConditionalOnProperty(name = "job.scheduler.type", havingValue = "spring-task", matchIfMissing = true)
    public TaskScheduler taskScheduler() {
        var scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(5);
        scheduler.setThreadNamePrefix("job-scheduler-");
        scheduler.setWaitForTasksToCompleteOnShutdown(true);
        scheduler.setAwaitTerminationSeconds(30);
        return scheduler;
    }

    /**
     * 条件化创建 Spring Task 调度器
     */
    @Bean
    @ConditionalOnProperty(name = "job.scheduler.type", havingValue = "spring-task", matchIfMissing = true)
    public JobScheduler springTaskScheduler(TaskScheduler taskScheduler) {
        return new SpringScheduler(taskScheduler);
    }

}
