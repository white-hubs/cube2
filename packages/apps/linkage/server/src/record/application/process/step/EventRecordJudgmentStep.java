package com.guhecloud.cube.biz.linkage.server.record.application.process.step;

import com.guhecloud.apps.linkage.metadata.enumeration.ActionBeforeTypeEnum;
import com.guhecloud.cube.biz.linkage.server.linkage.service.EventActionBeforeService;
import com.guhecloud.cube.biz.linkage.server.record.application.process.event.JudgmentTimeoutEvent;
import com.guhecloud.cube.biz.linkage.server.record.application.process.model.ProcessContext;
import com.guhecloud.cube.biz.linkage.server.record.application.process.model.result.ProcessResult;
import com.guhecloud.cube.biz.linkage.server.record.infrastructure.client.judgment.EventJudgmentService;
import com.guhecloud.cube.biz.linkage.server.record.reconfirmation.domain.entity.EventJudgmentDelayTask;
import com.guhecloud.cube.biz.linkage.server.record.reconfirmation.domain.enumeration.AiReconfirmResultEnum;
import com.guhecloud.cube.biz.linkage.server.record.reconfirmation.domain.valueobject.JudgmentConfig;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Slf4j
@Order(1)
@Component
public class EventRecordJudgmentStep implements EventRecordProcessStep {

    private final EventActionBeforeService eventActionBeforeService;

    private final EventJudgmentService eventJudgmentService;

    private final ApplicationEventPublisher applicationEventPublisher;

    private static final String STEP_NAME = "EVENT_ALARM_JUDGMENT_STEP";

    private static final Integer DEFAULT_TIMEOUT_SECONDS = 30;

    public EventRecordJudgmentStep(EventActionBeforeService eventActionBeforeService,
                                   EventJudgmentService eventJudgmentService,
                                   ApplicationEventPublisher applicationEventPublisher
                                  ) {
        this.eventActionBeforeService = eventActionBeforeService;
        this.eventJudgmentService = eventJudgmentService;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public ProcessResult execute(ProcessContext context) {
        var eventActionBeforeList = eventActionBeforeService.getListByEventId(context.getEventRecord().getBaseInfo().getEventId());
        var judgmentResultOpt = eventJudgmentService.fetchResult(context.getEventRecord());
        if (CollectionUtils.isEmpty(eventActionBeforeList) && judgmentResultOpt.isEmpty()) {
            return ProcessResult.continued();
        }
        var eventJudgmentOpt = eventActionBeforeList.stream()
                .filter(eventActionBefore -> ActionBeforeTypeEnum.ALARM_JUDGMENT == eventActionBefore.getActionBeforeType()).findFirst();
        if (eventJudgmentOpt.isEmpty()) {
            return ProcessResult.continued();
        }
        var eventJudgmentConfig = JsonUtils.deserialize(eventJudgmentOpt.get().getActionBeforeConfig(), JudgmentConfig.class);
        if (Objects.isNull(eventJudgmentConfig)) {
            return ProcessResult.continued();
        }
        //如果超时则直接触发后续流程
        var timeoutDate = context.getEventRecord().getBaseInfo().getEventTime()
                .plusSeconds(Objects.isNull(eventJudgmentConfig.getJudgmentTimeoutSeconds()) ? DEFAULT_TIMEOUT_SECONDS
                                                                                             : eventJudgmentConfig.getJudgmentTimeoutSeconds());
        if (timeoutDate.isBefore(DateUtils.now())) {
            return ProcessResult.continued();
        }
        if (judgmentResultOpt.isEmpty()) {
            //未拥有二次确认信息，放入延时队列
            eventJudgmentService.submitDelayTask(new EventJudgmentDelayTask(context.getEventRecord().getBaseInfo().getId(),
                                                                            eventJudgmentConfig.getJudgmentTimeoutSeconds(),
                                                                            () -> onConfirmationTimeout(context)));
            return ProcessResult.stop("event record not has reConfirmResult");
        }
        if (AiReconfirmResultEnum.SUSPECTED_ALARM == judgmentResultOpt.get().getResult()) {
            return ProcessResult.stop("event record:" + judgmentResultOpt.get().getId() + "  not is SUSPECTED_ALARM");
        }
        //真实报警继续执行后续步骤
        return ProcessResult.continued();
    }

    @Override
    public String getName() {
        return STEP_NAME;
    }

    private void onConfirmationTimeout(ProcessContext context) {
        SessionContextHolder.requestTenant(context.getTenant());
        log.warn("Confirmation timeout for event judgment{}",
                 context.getEventRecord().getBaseInfo().getId());
        try {
            //发送超时事件
            applicationEventPublisher.publishEvent(new JudgmentTimeoutEvent(this, context.getEventRecord()));
        } catch (Exception e) {
            log.error("Confirmation timeout for event judgment error", e);
        } finally {
            SessionContextHolder.clearSessionContext();
        }
    }
}
