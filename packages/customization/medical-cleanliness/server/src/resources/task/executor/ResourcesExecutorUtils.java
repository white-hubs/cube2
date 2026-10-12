package com.guhecloud.cube.srv.gen.medical.cleanliness.server.resources.task.executor;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.task.rejected.RuleIssuedAbortPolicy;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 资源管理线程池工具
 * (目前只提供获取默认的线程池)
 */
public class ResourcesExecutorUtils {
    private static final Integer DEFAULT_CORE_POOL_SIZE = 1;
    private static final Integer DEFAULT_MAXIMUM_POOL_SIZE = 2;
    private static final Long DEFAULT_KEEP_ALIVE_TIME = 3L;
    private static final Integer DEFAULT_QUEUE_SIZE = 5000;
    private static final String DEFAULT_POOL_NAME = "medical-cleanliness-resources-default";
    private static final ThreadPoolExecutor DEFAULT_EXECUTOR;
    static {
        DEFAULT_EXECUTOR = new ThreadPoolExecutor(
                DEFAULT_CORE_POOL_SIZE,
                DEFAULT_MAXIMUM_POOL_SIZE,
                DEFAULT_KEEP_ALIVE_TIME,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(DEFAULT_QUEUE_SIZE),
                new ThreadFactoryBuilder().setNameFormat(DEFAULT_POOL_NAME + "-%d").build(),
                new RuleIssuedAbortPolicy());
    }

    private ResourcesExecutorUtils(){}

    /**
     * 获取默认线程池
     * @return 线程池
     */
    public static ThreadPoolExecutor getDefaultExecutor(){
        return DEFAULT_EXECUTOR;
    }
}
