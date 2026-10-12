package com.guhecloud.cube.sn.meattrade.plugin.dbc.task.execute;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.guhecloud.cube.sn.meattrade.plugin.common.entity.PorkArrange;
import com.guhecloud.cube.sn.meattrade.plugin.common.enumeration.PorkArrangePushDataStatusEnum;
import com.guhecloud.cube.sn.meattrade.plugin.common.service.PorkArrangePushDataService;
import com.guhecloud.cube.sn.meattrade.plugin.dbc.task.TallyPushTask;
import com.guhecloud.platform.base.context.ApplicationContextHolder;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.event.src.annotation.EventBusListen;
import com.guhecloud.platform.event.src.common.SysReadEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Order(999)
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@EventBusListen(order = 1)
public class TallyPushExecute {

    private static final int BLOCK_QUEUE_SIZE = 100;

    private final PorkArrangePushDataService porkArrangePushDataService;

    private final BlockingQueue<PorkArrange> blockingQueue = new LinkedBlockingQueue<>(BLOCK_QUEUE_SIZE);

    @Subscribe
    @AllowConcurrentEvents
    public void initDispatchThread(SysReadEvent event) {
        //每次初始化后。修复数据异常状态
        porkArrangePushDataService.resetInProcessingStatus();
        Thread unPushTaskThread = new Thread(this::getUnPushTasks);
        unPushTaskThread.setName("unPushTaskThread");
        unPushTaskThread.setDaemon(true);
        unPushTaskThread.start();
        Thread startDispatchUnPushTaskThread = new Thread(this::startDispatchUnPushTasks);
        startDispatchUnPushTaskThread.setName("startDispatchUnPushTaskThread");
        startDispatchUnPushTaskThread.setDaemon(true);
        startDispatchUnPushTaskThread.start();
    }

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
            new ThreadFactoryBuilder().setNameFormat("daBaiCaiTallyPush-pool-%d").build(),
            //拒绝策略，如果线程满了，线程池就会拒绝,被拒绝的任务的处理程序，该处理程序以静默方式丢弃
            new TalyPushRejectedExecutionHandler()
    );

    private void getUnPushTasks() {
        while (true) {
            try {
                if (this.blockingQueue.isEmpty()) {
                    List<PorkArrange> tallyPushPOS = new ArrayList<>();
                    try {
                        tallyPushPOS = this.porkArrangePushDataService.getPushPorkArranges(BLOCK_QUEUE_SIZE);
                    } catch (Exception e) {
                        log.error("查询待推送数据异常,", e);
                    }
                    if (!CollectionUtils.isEmpty(tallyPushPOS)) {
                        Set<Long> ids = tallyPushPOS.stream().map(PorkArrange::getRecordId).collect(Collectors.toSet());
                        //修改状态为下发中
                        this.porkArrangePushDataService.updatePushStatusByIds(ids, PorkArrangePushDataStatusEnum.PUSH_PROCESS);
                    }
                    for (PorkArrange tallyPushPO : tallyPushPOS) {
                        boolean offerSuccess = this.blockingQueue.offer(tallyPushPO);
                        if (!offerSuccess) {
                            log.warn("缓存区对列已满");
                            //
                            this.porkArrangePushDataService.updatePushStatusByIds(Collections.singleton(tallyPushPO.getRecordId()),
                                                                                  PorkArrangePushDataStatusEnum.UN_PUSH);
                        }
                    }
                }
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                // 捕获中断异常
                log.error("getUnPushTasks Thread was interrupted during sleep.");
                Thread.currentThread().interrupt();
            } catch (Exception e) {
                log.error("startDispatchUnPush getUnPushTasks error:{}", e.getMessage());
            }
        }
    }

    private void startDispatchUnPushTasks() {
        while (true) {
            try {
                PorkArrange tallyPushPO = blockingQueue.take();
                executor.submit(new TallyPushTask(tallyPushPO));
            } catch (InterruptedException e) {
                // 捕获中断异常
                log.error("startDispatchUnPush Thread was interrupted during take.");
                Thread.currentThread().interrupt();
            } catch (Exception e) {
                log.error("startDispatchUnPush submit error:{}", e.getMessage());
            }
        }
    }

    private static TallyPushTask getRunnableTask(FutureTask<?> futureTask
                                                ) throws NoSuchFieldException, IllegalAccessException {
        Field field = futureTask.getClass().getDeclaredField("callable");
        field.setAccessible(true);
        Object callable = field.get(futureTask);
        Field taskField = callable.getClass().getDeclaredField("task");
        taskField.setAccessible(true);
        return (TallyPushTask) taskField.get(callable);
    }

    public static class TalyPushRejectedExecutionHandler implements RejectedExecutionHandler {

        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            PorkArrangePushDataService porkArrangePushDataService = ApplicationContextHolder.getBean(PorkArrangePushDataService.class);
            if (r instanceof FutureTask) {
                FutureTask<?> futureTask = (FutureTask<?>) r;
                try {
                    TallyPushTask tallyPushTask = getRunnableTask(futureTask);
                    porkArrangePushDataService.updatePushStatusByIds(
                            Collections.singleton(tallyPushTask.getTallyPushPO().getRecordId()),
                            PorkArrangePushDataStatusEnum.UN_PUSH);
                    log.warn("{} processing rejected, convert back to UN_PUSH", tallyPushTask.getTallyPushPO().getTallyId());
                } catch (NoSuchFieldException e) {
                    log.error("Rejected convert TallyPushTask NoSuchField error :", e);
                } catch (IllegalAccessException e) {
                    log.error("Rejected convert TallyPushTask IllegalAccess error:", e);
                }
            }
        }
    }
}
