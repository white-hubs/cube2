package com.guhecloud.cube.biz.linkage.server.record.application.commandgateway;

import com.guhecloud.cube.biz.linkage.server.record.application.commandgateway.command.IncreaseEventRecordCommand;
import com.guhecloud.cube.biz.linkage.server.record.domain.event.CreateAssignEventRecordEvent;
import com.guhecloud.cube.biz.linkage.server.record.domain.factory.EventRecordFactory;
import com.guhecloud.cube.biz.linkage.server.record.infrastructure.repository.EventRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

/**
 * 事件记录命令操作
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EventRecordCommandService {

    private final EventRecordRepository eventRecordRepository;

    private final ApplicationEventPublisher applicationEventPublisher;

    /**
     * 创建事件记录
     *
     * @param command 创建命令
     */
    public void createAssignAlarmRecord(IncreaseEventRecordCommand command) {
        var eventRecord = EventRecordFactory.newAlarmRecord(command);
        this.eventRecordRepository.save(eventRecord);
        this.applicationEventPublisher.publishEvent(new CreateAssignEventRecordEvent(this, eventRecord));
    }
}
