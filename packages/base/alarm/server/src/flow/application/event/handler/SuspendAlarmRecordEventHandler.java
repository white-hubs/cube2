package com.guhecloud.cube.base.alarm.server.flow.application.event.handler;

import com.guhecloud.cube.base.alarm.server.flow.application.AlarmRecordOperatorLogService;
import com.guhecloud.cube.base.alarm.server.flow.application.AlarmStateEventOperateService;
import com.guhecloud.cube.base.alarm.server.flow.domain.enumeration.AlarmFileSceneCodeEnum;
import com.guhecloud.cube.base.alarm.server.flow.domain.enumeration.AlarmOperatorTypeEnum;
import com.guhecloud.cube.base.alarm.server.flow.domain.enumeration.AlarmStateEventEnum;
import com.guhecloud.cube.base.alarm.server.flow.domain.event.SuspendAlarmRecordEvent;
import com.guhecloud.cube.base.alarm.server.flow.infrastructure.mq.rocketmq.todo.AlarmTodoRocketMQ;
import com.guhecloud.cube.base.alarm.server.flow.infrastructure.mq.rocketmq.todo.AlarmWorkCenterRocketMQ;
import com.guhecloud.cube.base.file.client.FileOperateClient;
import com.guhecloud.cube.base.file.client.param.FileInduceParam;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SuspendAlarmRecordEventHandler {

    private final AlarmRecordOperatorLogService alarmRecordOperatorLogService;

    private final AlarmStateEventOperateService alarmStateEventOperateService;

    private final UserClient userClient;

    private final FileOperateClient fileOperateClient;

    @EventListener
    public void suspendAlarmRecord(final SuspendAlarmRecordEvent suspendAlarmRecordEvent) {
        UserDTO userDTO = this.userClient.getUser(SecurityContextHolder.getPrincipal().getId());
        //记录日志消息
        String operatorLogContent = "挂起:" + userDTO.getName() + "挂起了该报警数据";
        //挂起操作信息记录
        alarmStateEventOperateService.save(suspendAlarmRecordEvent.getAlarmRecord().getAlarmInfo()
                                                   .getId(), suspendAlarmRecordEvent.getAlarmRecord()
                                                   .getAlarmStatus()
                                                   .getAlarmStatus(), AlarmStateEventEnum.SUSPEND, suspendAlarmRecordEvent
                                                   .getSuspendAlarmRecordCommand());
        //记录日志消息
        this.alarmRecordOperatorLogService.save(suspendAlarmRecordEvent.getAlarmRecord().getAlarmInfo()
                                                        .getId(), AlarmOperatorTypeEnum.ALARM_SUSPEND, SecurityContextHolder
                                                        .getPrincipal().getId()
                                                        .toString(), userDTO.getName(), DateUtils.now(), operatorLogContent, null);

        //文件持久化
        if (Objects.nonNull(suspendAlarmRecordEvent.getSuspendAlarmRecordCommand())
            && CollectionUtils.isNotEmpty(suspendAlarmRecordEvent.getSuspendAlarmRecordCommand().getFileVOS())) {
            List<Long> fileIdList = suspendAlarmRecordEvent.getSuspendAlarmRecordCommand().getFileVOS()
                    .stream().map(FileVO::getId).collect(Collectors.toList());
            FileInduceParam fileInduceParam = new FileInduceParam();
            fileInduceParam.setFileIdList(fileIdList);
            fileInduceParam.setSceneCode(AlarmFileSceneCodeEnum.ALARM_RECORD_HANDLER.getCode());
            fileOperateClient.fileInduceSingleBody(fileInduceParam);
        }
        //发送待办变更消息
        AlarmTodoRocketMQ.sendAlarmStatusChangeTodo(suspendAlarmRecordEvent.getAlarmRecord(),
                                                    suspendAlarmRecordEvent.getAlarmRecord().getAlarmConfirmed()
                                                            .getAlarmConfirmedBy(),
                                                    DateUtils.now(),
                                                    suspendAlarmRecordEvent.getPreviousAlarmStatus());
        AlarmWorkCenterRocketMQ.sendAlarmStatusChangeTask(suspendAlarmRecordEvent.getAlarmRecord(),
                                                          suspendAlarmRecordEvent.getAlarmRecord().getAlarmConfirmed()
                                                                  .getAlarmConfirmedBy(),
                                                          DateUtils.now(),
                                                          suspendAlarmRecordEvent.getPreviousAlarmStatus());
    }
}
