package com.guhecloud.cube.base.message2.server.handler.config;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.guhecloud.cube.base.message2.server.handler.exeception.MsgSendTaskRejectedExecutionHandler;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class HandlerThreadPoolConfig {

    private static final String PRE_FIX = "guheyun-msg.";

    /**
     * 业务：处理某个渠道的某种类型消息的线程池
     * 配置：不丢弃消息，核心线程数不会随着keepAliveTime而减少(不会被回收)
     *
     * @return
     */
    public static ThreadPoolExecutor getExecutor(String channelCode) {
        return new ThreadPoolExecutor(
                // 核心线程数
                4,
                //maximumPoolSize: 池中允许的最大的线程数
                20,
                //keepAliveTime: 当线程数大于核心线程数的时候，线程在最大多长时间没有接到新任务就会终止释放， 最终线程池维持在 corePoolSize 大小
                50,
                //时间单位
                TimeUnit.SECONDS,
                //workQueue: 阻塞队列，用来存储等待执行的任务，如果当前对线程的需求超过了 corePoolSize 大小，就会放在这里等待空闲线程执行。
                new LinkedBlockingDeque<>(100000),
                //threadFactory: 创建线程的工厂，比如指定线程名等
                new ThreadFactoryBuilder().setNameFormat(PRE_FIX + channelCode).build(),
                //自定义拒绝策略
                new MsgSendTaskRejectedExecutionHandler()
        );
    }
}
