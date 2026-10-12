package com.guhecloud.cube.base.alarm.server.state.operate.impl;

import com.guhecloud.cube.base.alarm.server.flow.application.commandgateway.command.AssignAlarmRecordCommand;
import com.guhecloud.cube.base.alarm.server.flow.application.commandgateway.command.HandlerAlarmRecordCommand;
import com.guhecloud.cube.base.alarm.server.flow.application.commandgateway.command.SuspendAlarmRecordCommand;
import com.guhecloud.cube.base.alarm.server.flow.application.commandgateway.command.TransferAlarmRecordCommand;
import com.guhecloud.cube.base.alarm.server.flow.domain.aggregates.AlarmRecord;
import com.guhecloud.cube.base.alarm.server.flow.domain.valueobject.AlarmHandler;
import com.guhecloud.cube.base.alarm.server.flow.domain.valueobject.AlarmReason;
import com.guhecloud.cube.base.alarm.server.state.AlarmStateContext;
import com.guhecloud.cube.base.alarm.server.state.operate.AbstractAlarmStateOperateService;
import com.guhecloud.platform.base.standard.exception.ResourceConflictException;
import com.guhecloud.platform.base.util.DateUtils;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Objects;

/**
 * 已处理状态的操作
 *
 * @author lanchun
 * @date 2023/2/21 9:10
 */
@Slf4j
public class HandlerAlarmOperateService extends AbstractAlarmStateOperateService {

    public HandlerAlarmOperateService(AlarmStateContext alarmStateContext) {
        super(alarmStateContext);
    }

    @Override
    public void confirmedAlarmRecord(AlarmRecord alarmRecord) {
        throw new ResourceConflictException("报警已确认请勿重复操作");
    }

    @Override
    public void transferAlarmRecord(AlarmRecord alarmRecord, TransferAlarmRecordCommand param) {
        throw new ResourceConflictException("当前状态不支持移交");
    }

    @Override
    public void assignAlarmRecord(AlarmRecord alarmRecord, AssignAlarmRecordCommand param) {
        throw new ResourceConflictException("当前状态不支持指派");
    }

    @Override
    public void suspendAlarmRecord(AlarmRecord alarmRecord, SuspendAlarmRecordCommand param) {
        throw new ResourceConflictException("当前状态不支持挂起");
    }

    @Override
    public void handlerAlarmRecord(AlarmRecord alarmRecord, HandlerAlarmRecordCommand param) {
        throw new ResourceConflictException("预警信息已处理");
    }

    public static void buildHandlerAlarmRecord(AlarmRecord alarmRecord, HandlerAlarmRecordCommand param) {
        LocalDateTime handlerTime = DateUtils.now();
        String userId = Objects.nonNull(param.getHandlerUser().getId()) ? param.getHandlerUser().getId().toString() : null;
        alarmRecord.settingAlarmHandler(new AlarmHandler(
                userId,
                param.getHandlerUser().getUserName(),
                handlerTime,
                param.getAlarmHandlerResult(),
                Objects.nonNull(param.getAlarmReason()) && Objects.nonNull(param.getAlarmReason().getId())
                ? AlarmReason.getAlarmReasonById(param.getAlarmReason().getId()) : null,
                Objects.nonNull(param.getAlarmHandlerResult()) ? param.getAlarmHandlerResult().getDescription() : null,
                DateUtils.between(alarmRecord.getAlarmInfo().getAlarmTime(), handlerTime, ChronoUnit.SECONDS)));
        //重置报警处理人
        alarmRecord.changeAlarmHandlerUsers(Collections.singletonList(param.getHandlerUser()));
    }
}
