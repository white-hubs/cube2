package com.guhecloud.cube.gm.meattrade.plugin.facade.gm.job;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.guhecloud.cube.gm.meattrade.plugin.facade.gm.rocketmq.producer.GmMeattradeTallyPushProducer;
import com.guhecloud.cube.gm.meattrade.plugin.tally.application.TallyPushService;
import com.guhecloud.cube.gm.meattrade.plugin.tally.domain.po.TallyPushPO;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.event.src.annotation.EventBusListen;
import com.guhecloud.platform.event.src.common.SysReadEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@ConditionalOnBean(name = "guangMingRocketMQProperties")
@EventBusListen(order = 1)
public class TallyPushFailedJob {

    private static final int BLOCK_QUEUE_SIZE = 100;

    private final TallyPushService tallyPushService;

    private final GmMeattradeTallyPushProducer gmMeattradeTallyPushProducer;

    private final BlockingQueue<TallyPushPO> blockingQueue = new LinkedBlockingQueue<>(BLOCK_QUEUE_SIZE);

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
            new ThreadFactoryBuilder().setNameFormat("tallyPush-pool-%d").build(),
            //拒绝策略，如果线程满了，线程池就会拒绝,被拒绝的任务的处理程序，该处理程序以静默方式丢弃
            new ThreadPoolExecutor.DiscardPolicy()
    );

    @Subscribe
    @AllowConcurrentEvents
    public void initDispatchThread(SysReadEvent event) {
        var unPushTaskThread = new Thread(this::pushIotEventFileQueue);
        unPushTaskThread.setName("gmUnPushTaskThread");
        unPushTaskThread.setDaemon(true);
        unPushTaskThread.start();
        var startDispatchUnPushTaskThread = new Thread(this::startDispatchUnPushTasks);
        startDispatchUnPushTaskThread.setName("gmStartDispatchUnPushTaskThread");
        startDispatchUnPushTaskThread.setDaemon(true);
        startDispatchUnPushTaskThread.start();
    }

    private void pushIotEventFileQueue() {
        while (true) {
            try {
                SessionContextHolder.clearSessionContext();
                if (this.blockingQueue.isEmpty()) {
                    var tallyPushPOS = this.tallyPushService.getNoTallyPushed(BLOCK_QUEUE_SIZE);
                    for (TallyPushPO tallyPushPO : tallyPushPOS) {
                        var offerSuccess = this.blockingQueue.offer(tallyPushPO);
                        if (!offerSuccess) {
                            log.warn("缓存区对列已满");
                            break;
                        }
                    }
                }
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                // 捕获中断异常
                log.error("getGmUnPushTasks Thread was interrupted during sleep.");
                Thread.currentThread().interrupt();
            } catch (Exception e) {
                log.error("startGmDispatchUnPush getUnPushTasks error:{}", e.getMessage());
            }
        }
    }

    private void startDispatchUnPushTasks() {
        while (true) {
            try {
                var tallyPushPO = blockingQueue.take();
                executor.submit(new TallyPushJob(tallyPushPO));
            } catch (InterruptedException e) {
                // 捕获中断异常
                log.error("startGmDispatchUnPush Thread was interrupted during take.");
                Thread.currentThread().interrupt();
            } catch (Exception e) {
                log.error("startGmDispatchUnPush submit error:{}", e.getMessage());
            }
        }
    }

    private class TallyPushJob implements Runnable {

        private final TallyPushPO tallyPushPO;

        public TallyPushJob(TallyPushPO tallyPushPO) {
            this.tallyPushPO = tallyPushPO;
        }

        @Override
        public void run() {
            //光明推送
            gmMeattradeTallyPushProducer.sendMessage(this.tallyPushPO);
        }
    }
}
