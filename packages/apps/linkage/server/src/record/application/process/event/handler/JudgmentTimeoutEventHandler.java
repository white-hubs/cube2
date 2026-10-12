package com.guhecloud.cube.biz.linkage.server.record.application.process.event.handler;

import com.guhecloud.cube.biz.linkage.server.record.application.process.event.JudgmentTimeoutEvent;
import com.guhecloud.cube.biz.linkage.server.record.application.process.manager.EventProcessManager;
import com.guhecloud.cube.biz.linkage.server.record.application.process.step.EventRecordJudgmentStep;
import com.guhecloud.cube.biz.linkage.server.record.reconfirmation.infrastructure.repository.EventAiResultService;
import com.guhecloud.platform.base.context.SessionContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Slf4j
@Component
public class JudgmentTimeoutEventHandler {

    private final EventProcessManager eventProcessManager;

    private final EventAiResultService eventAiResultService;

    public JudgmentTimeoutEventHandler(EventProcessManager eventProcessManager, EventAiResultService eventAiResultService) {
        this.eventProcessManager = eventProcessManager;
        this.eventAiResultService = eventAiResultService;
    }

    @EventListener
    public void judgmentTimeoutHandler(final JudgmentTimeoutEvent event) {
        log.info("Processing timeout event for alarm {},tenant {}", event.getEventRecord().getBaseInfo().getId(), SessionContextHolder
                .getRequester().getTenant());
        var alarmAiResult = this.eventAiResultService.getEventAiResult(event.getEventRecord().getBaseInfo().getId());
        if (Objects.nonNull(alarmAiResult)) {
            log.warn("AI result already exists for alarm {}, skipping", event.getEventRecord().getBaseInfo().getId());
            return;
        }
        try {
            log.info("Continuing process for alarm {}", event.getEventRecord().getBaseInfo().getId());
            eventProcessManager.continueProcessWith(event.getEventRecord(), EventRecordJudgmentStep.class, SessionContextHolder
                    .getRequester()
                    .getTenant());
        } catch (Exception e) {
            log.error("处理报警失败,失败原因", e);
        }
    }
}
