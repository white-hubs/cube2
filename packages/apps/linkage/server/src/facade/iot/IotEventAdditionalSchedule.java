package com.guhecloud.cube.biz.linkage.server.facade.iot;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.guhecloud.apps.linkage.metadata.enumeration.VideoActionTypeEnum;
import com.guhecloud.cube.base.file.client.FileClient;
import com.guhecloud.cube.base.file.client.param.FileParam;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.biz.linkage.server.facade.iot.dto.EventAdditionalNotifyDTO;
import com.guhecloud.cube.biz.linkage.server.facade.iot.message.IotEventFileConsumedMessage;
import com.guhecloud.cube.biz.linkage.server.linkage.entity.Event;
import com.guhecloud.cube.biz.linkage.server.linkage.service.EventService;
import com.guhecloud.cube.biz.linkage.server.linkage.strategy.video.entity.IotEventFileConsume;
import com.guhecloud.cube.biz.linkage.server.linkage.strategy.video.service.EventVideoFilePathService;
import com.guhecloud.cube.biz.linkage.server.linkage.strategy.video.service.IotEventFileConsumeService;
import com.guhecloud.cube.biz.linkage.server.util.FileUtils;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Requester;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.event.src.annotation.EventBusListen;
import com.guhecloud.platform.event.src.common.SysReadEvent;
import com.guhecloud.platform.job.jobtask.annotation.JobAnnotation;
import com.guhecloud.platform.redis.util.RedisUtils;
import com.guhecloud.platform.rocketmq.context.RocketMQContextHolder;
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
import java.util.stream.Collectors;

import static com.guhecloud.cube.biz.linkage.server.constants.FileSceneConstant.ALARM_SNAP_CAPTURE_PRE_FIX;

/**
 * iot附件消息持续消费任务
 *
 * @author lanchun
 * @date 2023/3/28 10:13
 */
@Slf4j
@Component
@EventBusListen(order = 2)
public class IotEventAdditionalSchedule {

    private static final Integer BLOCK_QUEUE_SIZE = 100;

    @Autowired
    private IotEventFileConsumeService iotEventFileConsumeService;

    @Autowired
    private EventService eventService;

    @Autowired
    private EventVideoFilePathService eventVideoFilePathService;

    @Autowired
    private FileClient fileClient;

    private final BlockingQueue<IotEventFileConsume> blockingQueue = new LinkedBlockingQueue<>(BLOCK_QUEUE_SIZE);

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
            new ThreadFactoryBuilder().setNameFormat("iotEventFileConsumer-pool-%d").build(),
            //拒绝策略，如果线程满了，线程池就会拒绝,被拒绝的任务的处理程序，该处理程序以静默方式丢弃
            new ThreadPoolExecutor.DiscardPolicy()
    );

    @Subscribe
    @AllowConcurrentEvents
    public void initDispatchThread(SysReadEvent event) {
        Thread stayEventFileThread = new Thread(this::pushIotEventFileQueue);
        stayEventFileThread.setName("stayEventFileThread");
        stayEventFileThread.setDaemon(true);
        stayEventFileThread.start();
        Thread startDispatchSatayEventFileTaskThread = new Thread(this::startDispatchStayEventFileTasks);
        startDispatchSatayEventFileTaskThread.setName("startDispatchSatayEventFileTaskThread");
        startDispatchSatayEventFileTaskThread.setDaemon(true);
        startDispatchSatayEventFileTaskThread.start();
    }

    private void pushIotEventFileQueue() {
        while (true) {
            try {
                SessionContextHolder.clearSessionContext();
                if (this.blockingQueue.isEmpty()) {
                    var iotEventFileConsumes = this.iotEventFileConsumeService.getStayEventFileConsume(BLOCK_QUEUE_SIZE);
                    for (var iotEventFileConsume : iotEventFileConsumes) {
                        boolean offerSuccess = this.blockingQueue.offer(iotEventFileConsume);
                        if (!offerSuccess) {
                            log.warn("缓存区对列已满");
                            break;
                        }
                    }
                }
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                // 捕获中断异常
                log.error("stayEventFileThread Thread was interrupted during sleep.");
                Thread.currentThread().interrupt();
            } catch (Exception e) {
                log.error("stayEventFileThread error:{}", e.getMessage());
            }

        }
    }

    private void startDispatchStayEventFileTasks() {
        while (true) {
            try {
                var iotEventFileConsume = blockingQueue.take();
                executor.submit(new IotEventAdditionalTask(iotEventFileConsume));
            } catch (InterruptedException e) {
                // 捕获中断异常
                log.error("startDispatchStayEventFile Thread was interrupted during take.");
                Thread.currentThread().interrupt();
            } catch (Exception e) {
                log.error("startDispatchStayEventFile submit error:{}", e.getMessage());
            }
        }
    }

    /**
     * 物联附件消息持续消费
     */
    private class IotEventAdditionalTask implements Runnable {

        public IotEventFileConsume iotEventFileConsume;

        public IotEventAdditionalTask(IotEventFileConsume iotEventFileConsume) {
            this.iotEventFileConsume = iotEventFileConsume;
        }

        @Override
        public void run() {
            SessionContextHolder.clearSessionContext();
            //使用分布式锁,避免被重复处理
            RedissonRedLock redissonRedLock = RedisUtils.getDistributedLock().tryLock("IOT_Event_Additional_synchronize:" +
                                                                                      iotEventFileConsume.getIotEventInstanceId(), Expiration.from(60000, TimeUnit.MILLISECONDS));
            try {
                if (Objects.nonNull(redissonRedLock)) {
                    //重写做一次查询,防止多线程情况下会新增2次附件信息
                    IotEventFileConsume realIotEventFileConsume = iotEventFileConsumeService.getById(iotEventFileConsume.getId());
                    //已处理就跳出,不再重复消费
                    if (Objects.isNull(realIotEventFileConsume) || Boolean.TRUE.equals(realIotEventFileConsume.getConsumed()) ||
                        (Objects.equals(realIotEventFileConsume.getIotEventInstanceId(), iotEventFileConsume.getIotEventInstanceId())) &&
                        Boolean.TRUE.equals(iotEventFileConsume.getConsumed())) {
                        return;
                    }
                    EventAdditionalNotifyDTO message = JsonUtils.deserialize(realIotEventFileConsume.getAdditional(), EventAdditionalNotifyDTO.class);
                    if (Objects.isNull(message)) {
                        return;
                    }
                    List<Event> events = eventService.getEventByIotEventIdAndDeviceId(realIotEventFileConsume.getIotEventInstanceId(), message
                            .getDeviceId().toString());
                    if (CollectionUtils.isEmpty(events)) {
                        return;
                    }
                    //获取iot上传的文件信息
                    FileVO fileVO = fileClient.getById(Long.valueOf(message.getAdditional().getAdditionalWithPicFileDes().getId()));
                    if (Objects.isNull(fileVO)) {
                        return;
                    }
                    //将文件信息转存到业务自己的桶中
                    for (Event event : events) {
                        SessionContextHolder.setRequester(Requester.builder().tenant(event.getTenant()).build());
                        FileVO file = FileUtils.uploadFileByPath(fileVO.getEndpointPath(), String.format(ALARM_SNAP_CAPTURE_PRE_FIX, event.getApplicationCode()));
                        SessionContextHolder.requestTenant(event.getTenant());
                        //附件存储
                        eventVideoFilePathService.saveIotFile(event.getId(), message.getDeviceId(),
                                                              message.getDeviceCode(), message.getDeviceName(),
                                                              file.getPath(),
                                                              file.getPath(),
                                                              message.getAdditional().getAdditionalWithPicFileDes().getType()
                                                                      .equals("mp4")
                                                              ? VideoActionTypeEnum.VIDEOTAPE
                                                              : VideoActionTypeEnum.PHOTOGRAPH);
                        //修改状态为已消费
                        realIotEventFileConsume.setConsumed(true);
                        iotEventFileConsumeService.updateById(realIotEventFileConsume);
                        //发送图片已成功消费事件
                        RocketMQContextHolder.send(IotEventFileConsumedMessage.TOPIC,
                                                   IotEventFileConsumedMessage.builder()
                                                           .iotEventId(realIotEventFileConsume.getIotEventInstanceId())
                                                           .eventId(event.getId())
                                                           .filePath(message.getAdditional().getAdditionalWithPicFileDes().getPath())
                                                           .build());
                    }
                }
            } catch (Exception e) {
                log.error("IOT事件附件执行失败事件实例ID为:{}", iotEventFileConsume.getIotEventInstanceId(), e);
            } finally {
                if (Objects.nonNull(redissonRedLock)) {
                    redissonRedLock.unlock();
                }
            }
        }
    }

    /**
     * 无用附件清理(每天清理一次)
     * 分批次查询删除，每次只加载 BATCH_SIZE 条数据，避免全量数据加载导致堆溢出
     */
    @JobAnnotation(name = "iotEventAdditionalClear", cron = "0 0 0 * * ?", desc = "联动-定时清理无用的事件附件")
    public void iotEventAdditionalClear(String param) {
        final int BATCH_SIZE = 200;
        var dateTime = DateUtils.offsetDay(DateUtils.now(), -1);
        List<IotEventFileConsume> batch;
        do {
            batch = this.iotEventFileConsumeService.getStayEventFileConsume(dateTime, BATCH_SIZE);
            if (CollectionUtils.isEmpty(batch)) {
                break;
            }
            var removeFileId = batch.stream()
                    .map(iotEventFileConsume -> Long.valueOf(JsonUtils
                            .deserialize(iotEventFileConsume.getAdditional(), EventAdditionalNotifyDTO.class)
                            .getAdditional()
                            .getAdditionalWithPicFileDes()
                            .getId()))
                    .distinct()
                    .collect(Collectors.toList());
            // 调用文件服务进行文件删除
            var fileParam = new FileParam();
            fileParam.setIds(removeFileId);
            this.fileClient.physicalDeleteByIds(fileParam);
            var ids = batch.stream().map(IotEventFileConsume::getId).collect(Collectors.toList());
            this.iotEventFileConsumeService.removeByIds(ids);
        } while (batch.size() == BATCH_SIZE);
    }
}
