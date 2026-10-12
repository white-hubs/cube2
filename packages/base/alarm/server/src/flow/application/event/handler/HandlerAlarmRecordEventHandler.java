package com.guhecloud.cube.base.alarm.server.flow.application.event.handler;

import com.guhecloud.cube.base.alarm.server.flow.application.AlarmRecordOperatorLogService;
import com.guhecloud.cube.base.alarm.server.flow.application.AlarmStateEventOperateService;
import com.guhecloud.cube.base.alarm.server.flow.domain.enumeration.AlarmFileSceneCodeEnum;
import com.guhecloud.cube.base.alarm.server.flow.domain.enumeration.AlarmOperatorTypeEnum;
import com.guhecloud.cube.base.alarm.server.flow.domain.enumeration.AlarmStateEventEnum;
import com.guhecloud.cube.base.alarm.server.flow.domain.event.HandlerAlarmRecordEvent;
import com.guhecloud.cube.base.alarm.server.flow.domain.valueobject.AlarmHandlerUser;
import com.guhecloud.cube.base.alarm.server.flow.infrastructure.client.AlarmDeviceFacadeService;
import com.guhecloud.cube.base.alarm.server.flow.infrastructure.message.AlarmMessageSender;
import com.guhecloud.cube.base.alarm.server.flow.infrastructure.mq.rocketmq.todo.AlarmTodoRocketMQ;
import com.guhecloud.cube.base.alarm.server.flow.infrastructure.mq.rocketmq.todo.AlarmWorkCenterRocketMQ;
import com.guhecloud.cube.base.file.client.FileOperateClient;
import com.guhecloud.cube.base.file.client.param.FileInduceParam;
import com.guhecloud.cube.base.file.client.vo.FileVO;
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
public class HandlerAlarmRecordEventHandler {

    private final AlarmRecordOperatorLogService alarmRecordOperatorLogService;

    private final AlarmStateEventOperateService alarmStateEventOperateService;

    private final AlarmMessageSender alarmMessageSender;

    private final FileOperateClient fileOperateClient;
    private final AlarmDeviceFacadeService alarmDeviceFacadeService;

    @EventListener
    public void handlerAlarmRecord(final HandlerAlarmRecordEvent handlerAlarmRecordEvent) {
        AlarmHandlerUser handlerUser = handlerAlarmRecordEvent.getHandlerAlarmRecordCommand().getHandlerUser();
        //处理操作信息记录
        this.alarmStateEventOperateService.save(handlerAlarmRecordEvent.getAlarmRecord().getAlarmInfo()
                                                        .getId(), handlerAlarmRecordEvent.getAlarmRecord()
                                                        .getAlarmStatus()
                                                        .getAlarmStatus(), AlarmStateEventEnum.PROCESSED, handlerAlarmRecordEvent
                                                        .getHandlerAlarmRecordCommand());
        //记录日志消息
        String operatorLogContent = "处理反馈:" + handlerUser.getUserName() + "反馈了处理结果";
        String createId =
                Objects.nonNull(SecurityContextHolder.getPrincipal(true)) ? SecurityContextHolder.getPrincipal().getAccount() : "system";
        this.alarmRecordOperatorLogService.save(handlerAlarmRecordEvent.getAlarmRecord().getAlarmInfo()
                                                        .getId(), AlarmOperatorTypeEnum.ALARM_HANDLER, createId,
                                                handlerUser.getUserName(), DateUtils.now(), operatorLogContent, null);
        //文件持久化
        if (Objects.nonNull(handlerAlarmRecordEvent.getHandlerAlarmRecordCommand())
            && CollectionUtils.isNotEmpty(handlerAlarmRecordEvent.getHandlerAlarmRecordCommand().getFileVOS())) {
            List<Long> fileIdList = handlerAlarmRecordEvent.getHandlerAlarmRecordCommand().getFileVOS()
                    .stream().map(FileVO::getId).collect(Collectors.toList());
            FileInduceParam fileInduceParam = new FileInduceParam();
            fileInduceParam.setFileIdList(fileIdList);
            fileInduceParam.setSceneCode(AlarmFileSceneCodeEnum.ALARM_RECORD_HANDLER.getCode());
            fileOperateClient.fileInduceSingleBody(fileInduceParam);
        }
        this.alarmMessageSender
                .alarmMqMsgSend(handlerAlarmRecordEvent.getAlarmRecord(), handlerAlarmRecordEvent.getAlarmRecord().getAlarmHandler()
                        .getAlarmHandlerTime());
        //发送待办变更消息
        AlarmTodoRocketMQ.sendAlarmHandlerTodo(handlerAlarmRecordEvent.getAlarmRecord(),
                                               handlerUser.getId(),
                                               DateUtils.now(),
                                               handlerAlarmRecordEvent.getPreviousAlarmStatus());
        //发送工作消息
        AlarmWorkCenterRocketMQ.sendAlarmHandlerWork(handlerAlarmRecordEvent.getAlarmRecord(),
                                                     handlerUser.getId(),
                                                     DateUtils.now(),
                                                     handlerAlarmRecordEvent.getPreviousAlarmStatus());
        //发送设备状态复位
        alarmDeviceFacadeService.sendDeviceStatusReset(handlerAlarmRecordEvent.getAlarmRecord());
        //设备消音
        alarmDeviceFacadeService.commandDeviceMuteStatus(handlerAlarmRecordEvent.getAlarmRecord());
    }

}
