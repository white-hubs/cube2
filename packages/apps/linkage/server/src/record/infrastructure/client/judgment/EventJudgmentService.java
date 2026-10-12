package com.guhecloud.cube.biz.linkage.server.record.infrastructure.client.judgment;

import com.guhecloud.cube.biz.linkage.server.record.domain.aggregates.EventRecord;
import com.guhecloud.cube.biz.linkage.server.record.reconfirmation.application.DefaultEventJudgmentDelayQueueService;
import com.guhecloud.cube.biz.linkage.server.record.reconfirmation.application.event.handler.IotReConfirmEventHandler;
import com.guhecloud.cube.biz.linkage.server.record.reconfirmation.domain.entity.EventJudgmentDelayTask;
import com.guhecloud.cube.biz.linkage.server.record.reconfirmation.domain.enumeration.AiReconfirmResultEnum;
import com.guhecloud.cube.biz.linkage.server.record.reconfirmation.domain.valueobject.JudgmentResult;
import com.guhecloud.cube.biz.linkage.server.record.reconfirmation.infrastructure.repository.EventAiResultService;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

@Component
@Service
public class EventJudgmentService {

    private final DefaultEventJudgmentDelayQueueService defaultEventJudgmentDelayQueueService;

    private final EventAiResultService eventAiResultService;

    private final IotReConfirmEventHandler iotReConfirmEventHandler;

    public EventJudgmentService(DefaultEventJudgmentDelayQueueService defaultEventJudgmentDelayQueueService,
                                EventAiResultService eventAiResultService,
                                IotReConfirmEventHandler iotReConfirmEventHandler
                               ) {
        this.defaultEventJudgmentDelayQueueService = defaultEventJudgmentDelayQueueService;
        this.eventAiResultService = eventAiResultService;
        this.iotReConfirmEventHandler = iotReConfirmEventHandler;
    }

    @Transactional(rollbackFor = Exception.class)
    public Optional<JudgmentResult> fetchResult(EventRecord eventRecord) {
        var eventAiResult = eventAiResultService.getEventAiResult(eventRecord.getBaseInfo().getId());
        if (Objects.nonNull(eventAiResult)) {
            return Optional.of(new JudgmentResult(eventRecord.getBaseInfo()
                                                          .getId(), AiReconfirmResultEnum.valueOf(eventAiResult.getResult())));
        }
        var orgResult = iotReConfirmEventHandler.getEventSnapshotReconfirmResult(eventRecord.getEventSnapshot()
                                                                                         .getSnapshot());
        var reConfirmResult = iotReConfirmEventHandler.convertIotEventResult(orgResult);
        if (Objects.isNull(reConfirmResult)) {
            return Optional.empty();
        }
        eventAiResultService.addEventAiResult(eventRecord, orgResult, reConfirmResult);
        return Optional.of(new JudgmentResult(eventRecord.getBaseInfo().getId(), reConfirmResult));
    }

    public void submitDelayTask(EventJudgmentDelayTask task) {
        defaultEventJudgmentDelayQueueService.submitDelayTask(task);
    }

    public void cancelDelayTask(Long id) {
        defaultEventJudgmentDelayQueueService.cancelDelayTask(id);
    }
}
