package com.guhecloud.cube.biz.linkage.server.record.application.event.handler;

import com.guhecloud.cube.biz.linkage.server.record.application.process.manager.EventProcessManager;
import com.guhecloud.cube.biz.linkage.server.record.domain.event.CreateAssignEventRecordEvent;
import com.guhecloud.platform.base.context.SessionContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class IncreaseEventRecordEventHandler {

    private final EventProcessManager eventProcessManager;

    @EventListener
    public void createEventRecord(final CreateAssignEventRecordEvent event) {
        eventProcessManager.executeProcess(event.getEventRecord(), SessionContextHolder.getRequester().getTenant());
    }
}
