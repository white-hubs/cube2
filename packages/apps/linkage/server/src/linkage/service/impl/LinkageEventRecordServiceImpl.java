package com.guhecloud.cube.biz.linkage.server.linkage.service.impl;

import com.guhecloud.cube.base.alarm.client.SatAlarmLevelClient;
import com.guhecloud.cube.biz.linkage.server.linkage.entity.Event;
import com.guhecloud.cube.biz.linkage.server.linkage.service.LinkageEventRecordService;
import com.guhecloud.cube.biz.linkage.server.linkage.strategy.alarmnotify.dto.LinkageAlarmTargetDTO;
import com.guhecloud.cube.biz.linkage.server.record.application.commandgateway.EventRecordCommandService;
import com.guhecloud.cube.biz.linkage.server.record.application.commandgateway.command.IncreaseEventRecordCommand;
import com.guhecloud.platform.base.util.JsonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class LinkageEventRecordServiceImpl implements LinkageEventRecordService {

    private final EventRecordCommandService eventRecordCommandService;

    private final SatAlarmLevelClient satAlarmLevelClient;

    @Override
    public void addEventRecord(Event event) {
        var eventConfig = JsonUtils.deserialize(event.getEventConfig(), LinkageAlarmTargetDTO.class);
        var command = new IncreaseEventRecordCommand();
        var level = satAlarmLevelClient.getAlarmLevel(event.getTriggerLevelId());
        command.setName(event.getName());
        command.setEventId(event.getId());
        command.setEventTime(event.getTriggerTime());
        command.setLevelId(event.getTriggerLevelId());
        command.setLevelCode(level.getCode());
        command.setLevelName(level.getName());
        if (Objects.nonNull(eventConfig)) {
            command.setDeviceId(eventConfig.getTriggerTargetKey());
            command.setDeviceCode(eventConfig.getTriggerTargetCode());
            command.setDeviceName(eventConfig.getTriggerTargetName());
            command.setDeviceAreaName(eventConfig.getAreaName());
            command.setDeviceLntAndLat(eventConfig.getTriggerTargetLongitude());
            command.setDeviceAddress(eventConfig.getTriggerTargetAddress());
            command.setEventTypeId(eventConfig.getEventTypeId());
            command.setEventTypeCode(eventConfig.getEventTypeCode());
            command.setEventTypeName(eventConfig.getEventTypeName());
        }
        command.setApplicationCode(event.getApplicationCode());
        command.setApplicationName(event.getApplicationName());
        command.setSnapshot(event.getSnapshot());
        eventRecordCommandService.createAssignAlarmRecord(command);
    }
}
