package com.guhecloud.cube.biz.meattrade.server.pork.job;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import com.guhecloud.cube.biz.meattrade.server.pork.dao.PorkArrangeDetailLogMapper;
import com.guhecloud.cube.biz.meattrade.server.pork.entity.PorkArrangeDetailLog;
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
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 理货附件转存持续消费任务
 *
 * @author lanchun
 * @date 2023/3/28 10:13
 */
@Slf4j
@Component
@EventBusListen(order = 2)
public class ProkArrangeDetailAdditionalSchedule {

    private static final Integer BLOCK_QUEUE_SIZE = 100;

    @Autowired
    private PorkArrangeDetailLogMapper porkArrangeDetailLogMapper;

    @Autowired
    private FileTransfer fileTransfer;

    private static final Integer TRANSFER_NUMBER = 60;

    private final BlockingQueue<PorkArrangeDetailLog> blockingQueue = new LinkedBlockingQueue<>(BLOCK_QUEUE_SIZE);

    @Subscribe
    @AllowConcurrentEvents
    public void initDispatchThread(SysReadEvent event) {
        var nonTransferFiles = new Thread(this::pushIotEventFileDetailQueue);
        nonTransferFiles.setName("nonTransferPorkArrangeDetailFiles");
        nonTransferFiles.setDaemon(true);
        nonTransferFiles.start();
        var startDispatchPorkArrangeNonTransferFileThread = new Thread(this::startDispatchNonTransferPorkArrangeDetailFileTask);
        startDispatchPorkArrangeNonTransferFileThread.setName("startDispatchNonTransferPorkArrangeFileTread");
        startDispatchPorkArrangeNonTransferFileThread.setDaemon(true);
        startDispatchPorkArrangeNonTransferFileThread.start();
    }

    private void pushIotEventFileDetailQueue() {
        while (true) {
            try {
                SessionContextHolder.clearSessionContext();
                if (this.blockingQueue.isEmpty()) {
                    var porkArrangeDetailLogs = this.porkArrangeDetailLogMapper.selectNonTransferFiles(BLOCK_QUEUE_SIZE, TRANSFER_NUMBER);
                    for (var porkArrangeDetailLog : porkArrangeDetailLogs) {
                        var offerSuccess = this.blockingQueue.offer(porkArrangeDetailLog);
                        if (!offerSuccess) {
                            log.warn("缓存区对列已满");
                            break;
                        }
                    }
                }
                TimeUnit.SECONDS.sleep(60);
            } catch (InterruptedException e) {
                // 捕获中断异常
                log.error("getPorkArrangeDetailNonTransferFile Thread was interrupted during sleep.");
                Thread.currentThread().interrupt();
            } catch (Exception e) {
                log.error("getPorkArrangeDetailNonTransferFile error:{}", e.getMessage());
            }
        }

    }

    private void startDispatchNonTransferPorkArrangeDetailFileTask() {
        while (true) {
            try {
                var porkArrangeDetailLog = blockingQueue.take();
                transferFile(porkArrangeDetailLog);
            } catch (InterruptedException e) {
                // 捕获中断异常
                log.error("startDispatchNonTransferPorkArrangeDetailFile Thread was interrupted during take.");
                Thread.currentThread().interrupt();
            } catch (Exception e) {
                log.error("startDispatchNonTransferPorkArrangeDetailFile submit error:{}", e.getMessage());
            }
        }
    }

    public void transferFile(PorkArrangeDetailLog porkArrangeDetailLog) {
        //使用分布式锁,避免被重复处理
        RedissonRedLock redissonRedLock = RedisUtils.getDistributedLock().tryLock(
                "IOT_Event_Additional_synchronize:" + porkArrangeDetailLog.getId(), Expiration.from(60000, TimeUnit.MILLISECONDS));
        try {
            if (Objects.nonNull(redissonRedLock)) {
                SessionContextHolder.requestTenant(porkArrangeDetailLog.getTenant());
                //重写做一次查询,防止多线程情况下会新增2次附件信息
                PorkArrangeDetailLog porkArrangeDetailLog1 = porkArrangeDetailLogMapper.selectById(porkArrangeDetailLog.getId());
                //已处理就跳出,不再重复消费
                if (Objects.equals(Boolean.TRUE, porkArrangeDetailLog1.getTransferStatus())) {
                    return;
                }
                String[] picPaths = porkArrangeDetailLog1.getPicPaths().split(",");
                //文件转存
                List<Long> fileIds = fileTransfer.transferFiles(picPaths, new String[0], porkArrangeDetailLog1.getTenant());
                String transferIds = fileIds.stream().map(String::valueOf).collect(Collectors.joining(","));
                porkArrangeDetailLogMapper.updateTransferFile(porkArrangeDetailLog1.getId(),
                                                              fileIds.size() == picPaths.length,
                                                              TRANSFER_NUMBER,
                                                              Objects.nonNull(porkArrangeDetailLog1.getTransferNumber())
                                                              ? porkArrangeDetailLog1.getTransferNumber() : 0,
                                                              transferIds);
            }
        } catch (Exception e) {
            log.error("理货不合格信息附件转存失败,理货详情实例ID为:{}", porkArrangeDetailLog.getId(), e);
            porkArrangeDetailLogMapper.updateTransferFile(porkArrangeDetailLog.getId(),
                                                          false,
                                                          TRANSFER_NUMBER,
                                                          Objects.nonNull(porkArrangeDetailLog.getTransferNumber())
                                                          ? porkArrangeDetailLog.getTransferNumber() : 0,
                                                          null);
        } finally {
            if (Objects.nonNull(redissonRedLock)) {
                redissonRedLock.unlock();
            }
        }
    }
}
