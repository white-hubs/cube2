package com.guhecloud.cube.base.alarm.server.state.operate.impl;

import com.guhecloud.cube.base.alarm.server.flow.application.commandgateway.command.AssignAlarmRecordCommand;
import com.guhecloud.cube.base.alarm.server.flow.application.commandgateway.command.HandlerAlarmRecordCommand;
import com.guhecloud.cube.base.alarm.server.flow.application.commandgateway.command.SuspendAlarmRecordCommand;
import com.guhecloud.cube.base.alarm.server.flow.application.commandgateway.command.TransferAlarmRecordCommand;
import com.guhecloud.cube.base.alarm.server.flow.domain.aggregates.AlarmRecord;
import com.guhecloud.cube.base.alarm.server.flow.domain.enumeration.AlarmStatusEnum;
import com.guhecloud.cube.base.alarm.server.flow.domain.valueobject.AlarmConfirmed;
import com.guhecloud.cube.base.alarm.server.flow.domain.valueobject.AlarmHandlerUser;
import com.guhecloud.cube.base.alarm.server.state.AlarmStateContext;
import com.guhecloud.cube.base.alarm.server.state.operate.AbstractAlarmStateOperateService;
import com.guhecloud.platform.base.standard.exception.ResourcePreconditionFailedException;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 待接收状态对应的操作
 *
 * @author lanchun
 * @date 2023/2/20 15:18
 */
@Slf4j
@Service
public class UnConfirmedAlarmOperateService extends AbstractAlarmStateOperateService {

    public UnConfirmedAlarmOperateService(AlarmStateContext alarmStateContext) {
        super(alarmStateContext);
    }

    @Override
    public void confirmedAlarmRecord(AlarmRecord alarmRecord) {
        alarmRecord.settingAlarmConfirmed(new AlarmConfirmed(SecurityContextHolder.getPrincipal().getId(), SecurityContextHolder
                .getPrincipal().getName(), DateUtils.now()));
        //确认接收报警后,重置报警处理人
        alarmRecord.changeSingleHandlerUser(SecurityContextHolder.getPrincipal().getId());
        //修改状态
        super.alarmStateContext.changeAlarmState(alarmRecord, AlarmStatusEnum.ALARM_PROGRESS_CONFIRMED);
    }

    @Override
    public void transferAlarmRecord(AlarmRecord alarmRecord, TransferAlarmRecordCommand param) {
        throw new ResourcePreconditionFailedException("待接收节点不支持移交");
    }

    @Override
    public void assignAlarmRecord(AlarmRecord alarmRecord, AssignAlarmRecordCommand param) {
        alarmRecord.settingAlarmConfirmed(new AlarmConfirmed(SecurityContextHolder.getPrincipal().getId(), SecurityContextHolder
                .getPrincipal().getName(), DateUtils.now()));
        //修改状态
        super.alarmStateContext.changeAlarmState(alarmRecord, AlarmStatusEnum.ALARM_PROGRESS_CONFIRMED);
        //确认接收报警后,重置报警处理人
        List<AlarmHandlerUser> alarmHandlerUsers = new ArrayList<>(param.getAssignUsers().size());
        param.getAssignUsers().stream().forEach(assignAlarmRecordUser -> {
            alarmHandlerUsers
                    .add(new AlarmHandlerUser(assignAlarmRecordUser.getUserId(), assignAlarmRecordUser.getUserName(), assignAlarmRecordUser
                            .getDepartmentId(), assignAlarmRecordUser.getDepartmentName()));
        });
        alarmRecord.changeAlarmHandlerUsers(alarmHandlerUsers);
    }

    @Override
    public void suspendAlarmRecord(AlarmRecord alarmRecord, SuspendAlarmRecordCommand param) {
        throw new ResourcePreconditionFailedException("待接收节点不支持挂起");
    }

    @Override
    public void handlerAlarmRecord(AlarmRecord alarmRecord, HandlerAlarmRecordCommand param) {
        HandlerAlarmOperateService.buildHandlerAlarmRecord(alarmRecord, param);
        //修改状态
        super.alarmStateContext.changeAlarmState(alarmRecord, AlarmStatusEnum.ALARM_HANDLER);
    }
}
