package com.guhecloud.platform.base.config;

import org.springframework.boot.autoconfigure.task.TaskExecutionProperties;
import org.springframework.boot.autoconfigure.task.TaskSchedulingProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * 线程池配置
 *
 * @author Woody
 * @date 2021/7/1
 */
@Configuration
@EnableScheduling
@EnableAsync(proxyTargetClass = true)
public class ThreadPoolConfigurer {
    @Bean
    @Primary
    public TaskExecutor taskExecutor(ThreadPoolProperties properties) {
        TaskExecutionProperties config = properties.getExecution();
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setThreadNamePrefix(config.getThreadNamePrefix());
        executor.setCorePoolSize(config.getPool().getCoreSize());
        executor.setMaxPoolSize(config.getPool().getMaxSize());
        executor.setQueueCapacity(config.getPool().getQueueCapacity());
        executor.setKeepAliveSeconds((int) config.getPool().getKeepAlive().getSeconds());
        executor.setWaitForTasksToCompleteOnShutdown(config.getShutdown().isAwaitTermination());
        executor.setAwaitTerminationSeconds((int) config.getShutdown().getAwaitTerminationPeriod().getSeconds());
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }

    @Bean
    public TaskScheduler taskScheduler(ThreadPoolProperties properties) {
        TaskSchedulingProperties config = properties.getScheduling();
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setThreadNamePrefix(config.getThreadNamePrefix());
        scheduler.setPoolSize(config.getPool().getSize());
        scheduler.setWaitForTasksToCompleteOnShutdown(config.getShutdown().isAwaitTermination());
        scheduler.setAwaitTerminationSeconds((int) config.getShutdown().getAwaitTerminationPeriod().getSeconds());
        scheduler.initialize();
        return scheduler;
    }
}
