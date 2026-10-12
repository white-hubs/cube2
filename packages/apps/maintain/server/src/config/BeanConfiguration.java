package com.guhecloud.cube.srv.gen.maintain.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author wql
 * @describe 单线程更新巡更任务状态
 * @date 2023/6/19 13:37
 */
@Component
public class BeanConfiguration {

    @Bean(name = "SingleThreadPool")
    public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor pool = new ThreadPoolTaskExecutor();
        pool.setCorePoolSize(1);
        pool.setMaxPoolSize(1);
        pool.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardPolicy());
        return pool;
    }

    /**
     * 离线巡检快照构建会发起大量数据库查询、远程二维码查询和 gzip 压缩，必须与通用异步线程池隔离。
     * <p>
     * 单线程按创建顺序构建会话；有界队列用于限制堆积的内存和数据库压力。队列满时由调用方将会话标记为失败，
     * 而不是使用 CallerRunsPolicy 回退到 HTTP 请求线程。
     */
    @Bean(name = "InspectOfflineSyncExecutor")
    public ThreadPoolTaskExecutor inspectOfflineSyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setThreadNamePrefix("inspect-offline-sync-");
        executor.setCorePoolSize(1);
        executor.setMaxPoolSize(1);
        executor.setQueueCapacity(20);
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(30);
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
        return executor;
    }
}
