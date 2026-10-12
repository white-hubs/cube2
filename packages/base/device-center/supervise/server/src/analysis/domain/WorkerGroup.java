package com.guhecloud.cube.srv.gen.device.supervise.server.analysis.domain;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author pengjian
 * @date 2022/7/25 18:34
 * @description
 */
@Slf4j
public class WorkerGroup {
    private List<Worker<CallbackResult>> workers;
    private Callback callback;
    private List<CallbackResult> callbackResults;
    private Integer tenantId;

    private ScheduledFuture<?> scheduledFuture;

    public WorkerGroup(Integer tenantId, Worker<CallbackResult>... workers) {
        this.workers = Arrays.asList(workers);
        this.tenantId = tenantId;
    }

    /**
     * 内置线程池，后续用户可以指定自己的线程池
     */
    private static final ThreadPoolExecutor executor = new ThreadPoolExecutor(
            // 核心线程数
            10,
            //maximumPoolSize: 池中允许的最大的线程数
            50,
            //keepAliveTime: 当线程数大于核心线程数的时候，线程在最大多长时间没有接到新任务就会终止释放， 最终线程池维持在 corePoolSize 大小
            50,
            //时间单位
            TimeUnit.SECONDS,
            //workQueue: 阻塞队列，用来存储等待执行的任务，如果当前对线程的需求超过了 corePoolSize 大小，就会放在这里等待空闲线程执行。
            new LinkedBlockingDeque<>(100000),
            //threadFactory: 创建线程的工厂，比如指定线程名等
            new ThreadFactory() {
                @Override
                public Thread newThread(Runnable r) {
                    ThreadGroup group = Thread.currentThread().getThreadGroup();
                    Thread t = new Thread(group, r);
                    //设置优先级
                    if (t.getPriority() != Thread.NORM_PRIORITY) {
                        t.setPriority(Thread.NORM_PRIORITY);
                    }
                    //设置错误处理
                    t.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
                        @Override
                        public void uncaughtException(Thread t, Throwable e) {
                            //自定义处理错误
                            System.out.println("factory捕获了错误--->>>" + t.getName() + e);
                            t.interrupt();
                        }
                    });
                    return t;

                }
            },
            //拒绝策略，如果线程满了，线程池就会使用拒绝策略。
            new ThreadPoolExecutor.AbortPolicy()
    );

    public void execute(Callback callback, int timeout, TimeUnit timeUnit) throws InterruptedException {
        this.callback = callback;
        // 执行worker
        List<Future<CallbackResult>> futures = executor.invokeAll(this.workers, timeout, timeUnit);

        /**
         * 直到group超时前，如果全部成功则合并结果，如果失败丢弃，继续执行下一个
         */
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
        scheduledFuture = scheduledExecutorService.scheduleWithFixedDelay(
                new ScheduleMonitoringResult(futures, this), 0, 2, TimeUnit.SECONDS
                                                                         );
    }

    public void execute(Callback callback) throws InterruptedException {
        execute(callback, 1, TimeUnit.MINUTES);
    }

    public void setCallbackResults(List<CallbackResult> callbackResults) {
        this.callbackResults = callbackResults;
        Integer score = 0;
        // 说明全部成功了
        for (int i = 0; i < callbackResults.size(); i++) {
            score += callbackResults.get(i).getScore();
        }
        this.callback.success(new CallbackResult(score, callbackResults.get(0).getHealthValueId(), callbackResults.get(0)
                .getDeviceId(), tenantId));

    }

    public void stop() {
        scheduledFuture.cancel(true);
    }
}
