package com.guhecloud.cube.biz.metric.server.metric.result.base.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 自定义线程池配置
 *
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/6/12 10:52
 * @Description:
 */
@Configuration
public class ThreadPoolConfig {

    private static final int CORE_POOL_SIZE = 10;
    private static final int MAX_POOL_SIZE = 20;
    private static final int QUEUE_CAPACITY = 128;
    private static final int KEEP_ALIVE_SECONDS = 180;

    @Bean("asyncMetricExecutor")
    public Executor asyncMetricExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //核心线程数
        executor.setCorePoolSize(CORE_POOL_SIZE);
        //最大线程数
        executor.setMaxPoolSize(MAX_POOL_SIZE);
        //缓冲队列
        executor.setQueueCapacity(QUEUE_CAPACITY);
        //允许线程空闲时间
        executor.setKeepAliveSeconds(KEEP_ALIVE_SECONDS);
        //线程池名前缀
        executor.setThreadNamePrefix("async-metric-pool-");
        //缓冲队列满了之后的拒绝策略：由调用线程处理
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }
}
