package com.guhecloud.platform.base.context;

import lombok.NonNull;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Supplier;

/**
 * Executor上下文工具类
 *
 * @author Woody
 * @date 2021/9/8
 */
@Component
public class ExecutorContextHolder implements ApplicationContextAware {
    /**
     * 多线程任务执行实例
     */
    private static Executor EXECUTOR;

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        EXECUTOR = applicationContext.getBean(TaskExecutor.class);
    }

    /**
     * 获取应用上下文中配置的Executor
     *
     * @return Executor对象
     */
    public static Executor getExecutor() {
        return Objects.requireNonNull(EXECUTOR, "Executor has not been initialized");
    }

    /**
     * 异步调用方法
     *
     * @param runnable Runnable实例
     *
     * @return CompletableFuture实例
     */
    public static CompletableFuture<Void> run(@NonNull Runnable runnable) {
        return CompletableFuture.runAsync(runnable, getExecutor());
    }

    /**
     * 异步调用方法
     *
     * @param supplier 方法实例
     * @param <U>      返回对象类型
     *
     * @return CompletableFuture实例
     */
    public static <U> CompletableFuture<U> supply(@NonNull Supplier<U> supplier) {
        return CompletableFuture.supplyAsync(supplier, getExecutor());
    }
}
