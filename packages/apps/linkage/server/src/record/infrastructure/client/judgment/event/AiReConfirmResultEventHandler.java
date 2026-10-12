package com.guhecloud.cube.biz.linkage.server.record.infrastructure.client.judgment.event;

import com.guhecloud.cube.biz.linkage.server.record.application.process.manager.EventProcessManager;
import com.guhecloud.cube.biz.linkage.server.record.application.process.step.EventRecordJudgmentStep;
import com.guhecloud.cube.biz.linkage.server.record.infrastructure.client.judgment.EventJudgmentService;
import com.guhecloud.cube.biz.linkage.server.record.infrastructure.repository.EventRecordRepository;
import com.guhecloud.cube.biz.linkage.server.record.reconfirmation.domain.event.AiReConfirmResultEvent;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.redis.util.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class AiReConfirmResultEventHandler {

    private final EventRecordRepository eventRecordRepository;

    private final EventJudgmentService eventJudgmentService;

    private final EventProcessManager eventProcessManager;

    public AiReConfirmResultEventHandler(EventRecordRepository eventRecordRepository,
                                         EventJudgmentService eventJudgmentService, EventProcessManager eventProcessManager
                                        ) {
        this.eventRecordRepository = eventRecordRepository;
        this.eventJudgmentService = eventJudgmentService;
        this.eventProcessManager = eventProcessManager;
    }

    @EventListener
    public void reConfirmResultEventHandler(final AiReConfirmResultEvent aiReConfirmResultEvent) {
        var eventRecord = this.eventRecordRepository.detail(aiReConfirmResultEvent.getEventId());
        eventRecord.getEventSnapshot().changeAlarmSnapshot(aiReConfirmResultEvent.getEventSnapshot());
        eventJudgmentService.cancelDelayTask(aiReConfirmResultEvent.getEventId());
        //启用分布式锁,防止处理时重复操作
        var alarmRecordLock = RedisUtils.getDistributedLock().tryLock(eventRecord.getBaseInfo().getId()
                                                                              .toString() + "ACTION_TRIGGER", Expiration
                                                                              .from(5000, TimeUnit.MILLISECONDS));
        try {
            if (Objects.nonNull(alarmRecordLock)) {
                eventRecordRepository.save(eventRecord);
                eventProcessManager.continueProcess(eventRecord, EventRecordJudgmentStep.class, SessionContextHolder.getRequester()
                        .getTenant());
            }
        } catch (Exception e) {
            log.error("处理报警失败,失败原因", e);
        } finally {
            if (Objects.nonNull(alarmRecordLock)) {
                alarmRecordLock.unlock();
            }
        }
    }
}
