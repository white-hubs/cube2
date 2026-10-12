package com.guhecloud.cube.base.alarm.server.state.operate.impl;

import com.guhecloud.cube.base.alarm.server.flow.application.commandgateway.command.AssignAlarmRecordCommand;
import com.guhecloud.cube.base.alarm.server.flow.application.commandgateway.command.HandlerAlarmRecordCommand;
import com.guhecloud.cube.base.alarm.server.flow.application.commandgateway.command.SuspendAlarmRecordCommand;
import com.guhecloud.cube.base.alarm.server.flow.application.commandgateway.command.TransferAlarmRecordCommand;
import com.guhecloud.cube.base.alarm.server.flow.domain.aggregates.AlarmRecord;
import com.guhecloud.cube.base.alarm.server.flow.domain.enumeration.AlarmStatusEnum;
import com.guhecloud.cube.base.alarm.server.flow.domain.valueobject.AlarmHandlerUser;
import com.guhecloud.cube.base.alarm.server.flow.domain.valueobject.AlarmSuspend;
import com.guhecloud.cube.base.alarm.server.state.AlarmStateContext;
import com.guhecloud.cube.base.alarm.server.state.operate.AbstractAlarmStateOperateService;
import com.guhecloud.platform.base.standard.exception.ResourceConflictException;
import com.guhecloud.platform.base.util.DateUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * 处理中状态对应的操作
 *
 * @author lanchun
 * @date 2023/2/20 17:17
 */
@Slf4j
public class ProgressAlarmOperateService extends AbstractAlarmStateOperateService {

    public ProgressAlarmOperateService(AlarmStateContext alarmStateContext) {
        super(alarmStateContext);
    }

    @Override
    public void confirmedAlarmRecord(AlarmRecord alarmRecord) {
        throw new ResourceConflictException("报警已确认请勿重复操作");
    }

    @Override
    public void transferAlarmRecord(AlarmRecord alarmRecord, TransferAlarmRecordCommand param) {
        alarmRecord.changeSingleHandlerUser(param.getUserId());
    }

    @Override
    public void assignAlarmRecord(AlarmRecord alarmRecord, AssignAlarmRecordCommand param) {
        List<AlarmHandlerUser> alarmHandlerUsers = new ArrayList<>(param.getAssignUsers().size());
        param.getAssignUsers().stream().forEach(assignAlarmRecordUser -> {
            alarmHandlerUsers
                    .add(new AlarmHandlerUser(assignAlarmRecordUser.getUserId(), assignAlarmRecordUser.getUserName(), assignAlarmRecordUser
                            .getDepartmentId(), assignAlarmRecordUser.getDepartmentName()));
        });
        //新增处理人
        alarmRecord.appendAlarmHandlerUsers(alarmHandlerUsers);
    }

    @Override
    public void suspendAlarmRecord(AlarmRecord alarmRecord, SuspendAlarmRecordCommand param) {
        alarmRecord.settingAlarmSuspend(new AlarmSuspend(DateUtils.now(), param.getEstimatedProcessTime()));
        //修改状态
        super.alarmStateContext.changeAlarmState(alarmRecord, AlarmStatusEnum.ALARM_SUSPEND);
    }

    @Override
    public void handlerAlarmRecord(AlarmRecord alarmRecord, HandlerAlarmRecordCommand param) {
        HandlerAlarmOperateService.buildHandlerAlarmRecord(alarmRecord, param);
        //修改状态
        super.alarmStateContext.changeAlarmState(alarmRecord, AlarmStatusEnum.ALARM_HANDLER);
    }
}
