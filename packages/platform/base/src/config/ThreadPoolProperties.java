package com.guhecloud.platform.base.config;

import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.Data;
import org.springframework.boot.autoconfigure.task.TaskExecutionProperties;
import org.springframework.boot.autoconfigure.task.TaskSchedulingProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

/**
 * 线程池配置属性
 *
 * @author Woody
 * @date 2021/7/1
 * @see ThreadPoolConfigurer
 */
@Data
@Configuration
@ConfigurationProperties("platform.task")
public class ThreadPoolProperties {
    /**
     * 任务执行线程池配置
     */
    private final TaskExecutionProperties execution = new TaskExecutionProperties() {
        {
            // 初始化线程池默认值
            this.setThreadNamePrefix("task-executor-");
            this.getPool().setCoreSize(ObjectUtils.CPU_COUNT);
            this.getPool().setMaxSize(ObjectUtils.CPU_COUNT * 2);
            this.getPool().setQueueCapacity(1000);
            this.getPool().setKeepAlive(Duration.ofSeconds(60));
            this.getShutdown().setAwaitTermination(true);
            this.getShutdown().setAwaitTerminationPeriod(Duration.ofSeconds(1));
        }
    };

    /**
     * 任务调度线程池配置
     */
    private final TaskSchedulingProperties scheduling = new TaskSchedulingProperties() {
        {
            // 初始化线程池默认值
            this.setThreadNamePrefix("task-scheduler-");
            this.getPool().setSize(ObjectUtils.CPU_COUNT);
            this.getShutdown().setAwaitTermination(true);
            this.getShutdown().setAwaitTerminationPeriod(Duration.ofSeconds(1));
        }
    };
}
