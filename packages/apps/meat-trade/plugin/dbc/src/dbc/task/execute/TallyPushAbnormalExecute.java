package com.guhecloud.cube.sn.meattrade.plugin.dbc.task.execute;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.guhecloud.cube.sn.meattrade.plugin.common.entity.PorkArrange;
import com.guhecloud.cube.sn.meattrade.plugin.common.service.PorkArrangePushDataService;
import com.guhecloud.cube.sn.meattrade.plugin.dbc.task.TallyPushTask;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.event.src.annotation.EventBusListen;
import com.guhecloud.platform.event.src.common.SysReadEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 推送失败的线程池执行器
 * 负责推送失败的理货数据进行重新推送
 *
 * @author lanchun
 * @date 2024/9/30 15:52
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@EventBusListen(order = 1)
public class TallyPushAbnormalExecute {

    private static final int BLOCK_QUEUE_SIZE = 100;

    private final PorkArrangePushDataService porkArrangePushDataService;

    private final BlockingQueue<PorkArrange> blockingQueue = new LinkedBlockingQueue<>(BLOCK_QUEUE_SIZE);

    private static final int RETRY_INTERVAL = 60; //每分钟重试一次

    private static final int MAX_RETRIES_HOURS = 4;//4小时内最大重试

    /**
     * 内置线程池，后续用户可以指定自己的线程池
     */
    private static final ThreadPoolExecutor executor = new ThreadPoolExecutor(
            // 核心线程数
            4,
            //maximumPoolSize: 池中允许的最大的线程数
            20,
            //keepAliveTime: 当线程数大于核心线程数的时候，线程在最大多长时间没有接到新任务就会终止释放， 最终线程池维持在 corePoolSize 大小
            50,
            //时间单位
            TimeUnit.SECONDS,
            //workQueue: 阻塞队列，用来存储等待执行的任务，如果当前对线程的需求超过了 corePoolSize 大小，就会放在这里等待空闲线程执行。
            new LinkedBlockingQueue<>(100),
            //threadFactory: 创建线程的工厂，比如指定线程名等
            new ThreadFactoryBuilder().setNameFormat("daBaiCaiTallyAbnormalPush-pool-%d").build(),
            //拒绝策略，如果线程满了，线程池就会拒绝,被拒绝的任务的处理程序，该处理程序以静默方式丢弃(由于是重试任务,丢了也就丢了等待下次执行)
            new ThreadPoolExecutor.DiscardPolicy()
    );

    @Subscribe
    @AllowConcurrentEvents
    public void initDispatchThread(SysReadEvent event) {
        new Thread(this::getPushFailTasks).start();
        new Thread(this::startDispatchFailPushTasks).start();
    }

    private void getPushFailTasks() {
        while (true) {
            try {
                if (this.blockingQueue.isEmpty()) {
                    var retryTime = LocalDateTime.now();
                    //倒推4小时内的失败数据
                    var maxRetry = DateUtils.toTimestamp(retryTime) - MAX_RETRIES_HOURS * RETRY_INTERVAL * 60 * 1000;
                    var maxRetryTime = DateUtils.parse(maxRetry);
                    List<PorkArrange> tallyPushPOS = new ArrayList<>();
                    try {
                        tallyPushPOS = this.porkArrangePushDataService.getPushFailPorkArranges(maxRetryTime, BLOCK_QUEUE_SIZE, retryTime);
                    } catch (Exception e) {
                        log.error("查询失败的推送数据异常,", e);
                    }
                    for (PorkArrange tallyPushPO : tallyPushPOS) {
                        boolean offerSuccess = this.blockingQueue.offer(tallyPushPO);
                        if (!offerSuccess) {
                            log.warn("缓存区对列已满");
                        }
                    }
                }
                TimeUnit.SECONDS.sleep(RETRY_INTERVAL);
            } catch (InterruptedException e) {
                log.error("获取推送失败的任务出现异常", e);
                // 捕获中断异常
                log.error("getPushFailTasks Thread was interrupted during sleep.");
                Thread.currentThread().interrupt();
            }
        }
    }

    private void startDispatchFailPushTasks() {
        while (true) {
            try {
                PorkArrange tallyPushPO = blockingQueue.take();
                executor.submit(new TallyPushTask(tallyPushPO));
            } catch (InterruptedException e) {
                // 捕获中断异常
                log.error("start DispatchFailPushTask Thread was interrupted during take.");
                Thread.currentThread().interrupt();
            }
        }
    }
}
