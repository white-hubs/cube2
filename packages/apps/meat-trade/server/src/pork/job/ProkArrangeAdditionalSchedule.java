package com.guhecloud.cube.biz.meattrade.server.pork.job;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.guhecloud.cube.biz.meattrade.server.pork.dao.PorkArrangeDetailMapper;
import com.guhecloud.cube.biz.meattrade.server.pork.entity.PorkArrangeDetail;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.event.src.annotation.EventBusListen;
import com.guhecloud.platform.event.src.common.SysReadEvent;
import com.guhecloud.platform.redis.util.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.redisson.RedissonRedLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 理货附件转存持续消费任务
 *
 * @author lanchun
 * @date 2023/3/28 10:13
 */
@Slf4j
@Component
@EventBusListen(order = 2)
public class ProkArrangeAdditionalSchedule {

    private static final Integer BLOCK_QUEUE_SIZE = 100;

    @Autowired
    private PorkArrangeDetailMapper porkArrangeDetailMapper;

    @Autowired
    private FileTransfer fileTransfer;

    private static final Integer TRANSFER_NUMBER = 60;

    private final BlockingQueue<PorkArrangeDetail> blockingQueue = new LinkedBlockingQueue<>(BLOCK_QUEUE_SIZE);

    /**
     * 内置线程池，后续用户可以指定自己的线程池
     */
    private final ThreadPoolExecutor executor = new ThreadPoolExecutor(
            // 核心线程数
            4,
            //maximumPoolSize: 池中允许的最大的线程数
            20,
            //keepAliveTime: 当线程数大于核心线程数的时候，线程在最大多长时间没有接到新任务就会终止释放， 最终线程池维持在 corePoolSize 大小
            50,
            //时间单位
            TimeUnit.SECONDS,
            //workQueue: 阻塞队列，用来存储等待执行的任务，如果当前对线程的需求超过了 corePoolSize 大小，就会放在这里等待空闲线程执行。
            new LinkedBlockingQueue<>(101),
            //threadFactory: 创建线程的工厂，比如指定线程名等
            new ThreadFactoryBuilder().setNameFormat("porkArrangeFileTransfer-pool-%d").build(),
            //拒绝策略，如果线程满了，线程池就会拒绝,被拒绝的任务的处理程序，该处理程序以静默方式丢弃
            new ThreadPoolExecutor.DiscardPolicy()
    );

    @Subscribe
    @AllowConcurrentEvents
    public void initDispatchThread(SysReadEvent event) {
        var nonTransferFiles = new Thread(this::pushIotEventFileQueue);
        nonTransferFiles.setName("nonTransferPorkArrangeFiles");
        nonTransferFiles.setDaemon(true);
        nonTransferFiles.start();
        var startDispatchPorkArrangeNonTransferFileThread = new Thread(this::startDispatchNonTransferPorkArrangeFileTask);
        startDispatchPorkArrangeNonTransferFileThread.setName("startDispatchNonTransferPorkArrangeFileTread");
        startDispatchPorkArrangeNonTransferFileThread.setDaemon(true);
        startDispatchPorkArrangeNonTransferFileThread.start();
    }

    private void pushIotEventFileQueue() {
        while (true) {
            try {
                SessionContextHolder.clearSessionContext();
                if (this.blockingQueue.isEmpty()) {
                    var porkArrangeDetails = this.porkArrangeDetailMapper.selectNonTransferFiles(BLOCK_QUEUE_SIZE, TRANSFER_NUMBER);
                    for (var porkArrangeDetail : porkArrangeDetails) {
                        var offerSuccess = this.blockingQueue.offer(porkArrangeDetail);
                        if (!offerSuccess) {
                            log.warn("缓存区对列已满");
                            break;
                        }
                    }
                }
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                // 捕获中断异常
                log.error("getPorkArrangeNonTransferFile Thread was interrupted during sleep.");
                Thread.currentThread().interrupt();
            } catch (Exception e) {
                log.error("getPorkArrangeNonTransferFile error:{}", e.getMessage());
            }
        }
    }

    private void startDispatchNonTransferPorkArrangeFileTask() {
        while (true) {
            try {
                var porkArrangeDetail = blockingQueue.take();
                executor.submit(new PorkArrangeFileTransferAdditionalTask(porkArrangeDetail));
            } catch (InterruptedException e) {
                // 捕获中断异常
                log.error("startDispatchNonTransferPorkArrangeFile Thread was interrupted during take.");
                Thread.currentThread().interrupt();
            } catch (Exception e) {
                log.error("startDispatchNonTransferPorkArrangeFile submit error:{}", e.getMessage());
            }
        }
    }

    /**
     * 理货图片转存任务
     */
    private class PorkArrangeFileTransferAdditionalTask implements Runnable {

        public PorkArrangeDetail porkArrangeDetail;

        public PorkArrangeFileTransferAdditionalTask(PorkArrangeDetail porkArrangeDetail) {
            this.porkArrangeDetail = porkArrangeDetail;
        }

        @Override
        public void run() {
            //使用分布式锁,避免被重复处理
            RedissonRedLock redissonRedLock = RedisUtils.getDistributedLock().tryLock(
                    "IOT_Event_Additional_synchronize:" + porkArrangeDetail.getId(), Expiration.from(60000, TimeUnit.MILLISECONDS));
            try {
                if (Objects.nonNull(redissonRedLock)) {
                    SessionContextHolder.requestTenant(porkArrangeDetail.getTenant());
                    //重写做一次查询,防止多线程情况下会新增2次附件信息
                    PorkArrangeDetail porkArrangeDetail = porkArrangeDetailMapper.selectById(this.porkArrangeDetail.getId());
                    //已处理就跳出,不再重复消费
                    if (Objects.equals(Boolean.TRUE, porkArrangeDetail.getTransferStatus())) {
                        return;
                    }
                    String[] picPaths = porkArrangeDetail.getPicPaths().split(",");
                    String[] preFileIds = porkArrangeDetail.getTransferFiles().split(",");
                    //文件转存
                    List<Long> fileIds = fileTransfer.transferFiles(picPaths, preFileIds, porkArrangeDetail.getTenant());
                    porkArrangeDetailMapper.updateTransferFile(porkArrangeDetail.getId(),
                                                               fileIds.size() == picPaths.length,
                                                               TRANSFER_NUMBER,
                                                               Objects.nonNull(porkArrangeDetail.getTransferNumber())
                                                               ? porkArrangeDetail.getTransferNumber() : 0);
                }
            } catch (Exception e) {
                log.error("理货附件转存失败,理货详情实例ID为:{}", porkArrangeDetail.getId(), e);
            } finally {
                if (Objects.nonNull(redissonRedLock)) {
                    redissonRedLock.unlock();
                }
            }
        }
    }
}
