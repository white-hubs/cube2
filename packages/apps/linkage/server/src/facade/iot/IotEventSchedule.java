package com.guhecloud.cube.biz.linkage.server.facade.iot;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.guhecloud.cube.base.alarm.client.AlarmRecordClient;
import com.guhecloud.cube.base.alarm.client.dto.AlarmRecordDTO;
import com.guhecloud.cube.biz.linkage.server.facade.iot.repository.entity.IotEventConsume;
import com.guhecloud.cube.biz.linkage.server.facade.iot.repository.service.IotEventConsumeService;
import com.guhecloud.cube.biz.linkage.server.linkage.entity.Event;
import com.guhecloud.cube.biz.linkage.server.linkage.service.EventService;
import com.guhecloud.cube.biz.linkage.server.rule.base.mq.EventSnapshotUpdateMessage;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Requester;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.redis.util.RedisUtils;
import com.guhecloud.platform.rocketmq.context.RocketMQContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.redisson.RedissonRedLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * iot事件快照持续消费任务
 */
@Slf4j
@Component
public class IotEventSchedule {

    private static final Integer BLOCK_QUEUE_SIZE = 100;

    @Autowired
    private IotEventConsumeService iotEventConsumeService;

    @Autowired
    private EventService eventService;

    @Autowired
    private AlarmRecordClient alarmRecordClient;

    private final BlockingQueue<IotEventConsume> blockingQueue = new LinkedBlockingQueue(BLOCK_QUEUE_SIZE);

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
            new ThreadFactoryBuilder().setNameFormat("iotEventSnapshotConsumer-pool-%d").build(),
            //拒绝策略，如果线程满了，线程池就会拒绝,被拒绝的任务的处理程序，该处理程序以静默方式丢弃
            new ThreadPoolExecutor.DiscardPolicy()
    );

    @Scheduled(fixedDelay = 2000)
    public void pushIotEventQueue() {
        SessionContextHolder.clearSessionContext();
        if (this.blockingQueue.isEmpty()) {
            List<IotEventConsume> iotEventConsumes = this.iotEventConsumeService.getStayEventConsume(BLOCK_QUEUE_SIZE);
            for (IotEventConsume iotEventConsume : iotEventConsumes) {
                boolean offerSuccess = this.blockingQueue.offer(iotEventConsume);
                if (!offerSuccess) {
                    log.warn("缓存区对列已满");
                    break;
                }
            }
        }
        while (true) {
            IotEventConsume iotEventConsume = blockingQueue.poll();
            if (Objects.isNull(iotEventConsume)) {
                break;
            }
            executor.submit(new IotEventSnapshotTask(iotEventConsume));
        }
    }

    /**
     * 物联事件快照消息持续消费
     */
    private class IotEventSnapshotTask implements Runnable {

        public IotEventConsume iotEventConsume;

        public IotEventSnapshotTask(IotEventConsume iotEventConsume) {
            this.iotEventConsume = iotEventConsume;
        }

        @Override
        public void run() {
            SessionContextHolder.clearSessionContext();
            //使用分布式锁,避免被重复处理
            RedissonRedLock redissonRedLock = RedisUtils.getDistributedLock().tryLock("IOT_Event_Snapshot_synchronize:" +
                                                                                      iotEventConsume.getIotEventInstanceId(), Expiration.from(60000, TimeUnit.MILLISECONDS));
            try {
                if (Objects.nonNull(redissonRedLock)) {
                    //重写做一次查询,防止多线程情况下会新增信息
                    IotEventConsume realIotEventConsume = iotEventConsumeService.getById(iotEventConsume.getId());
                    //已处理就跳出,不再重复消费
                    if (Objects.isNull(realIotEventConsume) || Boolean.TRUE.equals(realIotEventConsume.getConsumed()) ||
                        (Objects.equals(realIotEventConsume.getIotEventInstanceId(), iotEventConsume.getIotEventInstanceId())) &&
                        Boolean.TRUE.equals(realIotEventConsume.getConsumed())) {
                        return;
                    }
                    List<Event> events = eventService.getEventByIotEventId(realIotEventConsume.getIotEventInstanceId());
                    if (CollectionUtils.isEmpty(events)) {
                        return;
                    }
                    //将文件信息转存到业务自己的桶中
                    for (Event event : events) {
                        SessionContextHolder.setRequester(Requester.builder().tenant(event.getTenant()).build());
                        AlarmRecordDTO alarmRecordDTO = alarmRecordClient.getAlarmRecordByBusinessKey(String.valueOf(event.getId()));
                        if (Objects.isNull(alarmRecordDTO)) {
                            continue;
                        }
                        //修改状态为已消费
                        realIotEventConsume.setConsumed(true);
                        iotEventConsumeService.updateById(realIotEventConsume);
                        EventSnapshotUpdateMessage message = new EventSnapshotUpdateMessage();
                        message.setEventId(event.getId());
                        message.setSnapshot(realIotEventConsume.getAdditional());
                        RocketMQContextHolder.send(EventSnapshotUpdateMessage.TOPIC, message);
                    }
                }
            } catch (Exception e) {
                log.error("IOT事件快照执行失败事件实例ID为:{}", iotEventConsume.getIotEventInstanceId(), e);
            } finally {
                if (Objects.nonNull(redissonRedLock)) {
                    redissonRedLock.unlock();
                }
            }
        }
    }

    /**
     * 无用事件清理(每天清理一次)
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void iotEventAdditionalClear() {
        var dateTime = DateUtils.offsetDay(DateUtils.now(), -1);
        List<IotEventConsume> stayEventConsume = this.iotEventConsumeService.getStayEventConsume(dateTime);
        if (!CollectionUtils.isEmpty(stayEventConsume)) {
            List<Long> ids = stayEventConsume.stream().map(IotEventConsume::getId)
                    .collect(Collectors.toList());
            this.iotEventConsumeService.removeByIds(ids);
        }

    }
}
