package com.guhecloud.cube.base.alarm.server.flow.application.event.handler;

import com.guhecloud.apps.linkage.metadata.param.EventMessageSendParam;
import com.guhecloud.cube.base.alarm.server.flow.application.AlarmRecordOperatorLogService;
import com.guhecloud.cube.base.alarm.server.flow.application.commandgateway.command.AssignAlarmRecordUserCommand;
import com.guhecloud.cube.base.alarm.server.flow.application.querygateway.AlarmRecordQueryService;
import com.guhecloud.cube.base.alarm.server.flow.domain.enumeration.AlarmOperatorTypeEnum;
import com.guhecloud.cube.base.alarm.server.flow.domain.enumeration.AlarmStatusEnum;
import com.guhecloud.cube.base.alarm.server.flow.domain.event.AssignAlarmRecordEvent;
import com.guhecloud.cube.base.alarm.server.flow.domain.po.AlarmRecordPO;
import com.guhecloud.cube.base.alarm.server.flow.domain.valueobject.AlarmHandlerUser;
import com.guhecloud.cube.base.alarm.server.flow.infrastructure.message.AlarmMessageSender;
import com.guhecloud.cube.base.alarm.server.flow.infrastructure.mq.rocketmq.todo.AlarmTodoRocketMQ;
import com.guhecloud.cube.base.alarm.server.flow.infrastructure.mq.rocketmq.todo.AlarmWorkCenterRocketMQ;
import com.guhecloud.cube.biz.linkage.client.LinkageMessageClient;
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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AssignAlarmRecordEventHandler {

    private final AlarmRecordOperatorLogService alarmRecordOperatorLogService;

    private final UserClient userClient;

    private final AlarmMessageSender alarmMessageSender;

    private final AlarmRecordQueryService alarmRecordQueryService;

    private final LinkageMessageClient linkageMessageClient;

    @EventListener
    public void assignAlarmRecord(final AssignAlarmRecordEvent assignAlarmRecordEvent) {
        UserDTO userDTO = this.userClient.getUser(SecurityContextHolder.getPrincipal().getId());
        //记录日志消息
        String userNames = assignAlarmRecordEvent.getAssignAlarmRecordCommand().getAssignUsers().stream()
                .map(AssignAlarmRecordUserCommand::getUserName).distinct()
                .collect(Collectors.joining(","));
        String operatorLogContent = "指派:" + userDTO.getName() + "将报警任务指派给了" + userNames;
        //记录日志消息
        this.alarmRecordOperatorLogService.save(assignAlarmRecordEvent.getAlarmRecord().getAlarmInfo()
                                                        .getId(), AlarmOperatorTypeEnum.ALARM_ASSIGN, SecurityContextHolder
                                                        .getPrincipal().getId()
                                                        .toString(), userDTO.getName(), DateUtils.now(), operatorLogContent, null);
        assignAlarmNotify(assignAlarmRecordEvent);//报警通知
        assignAlarmCcNotify(assignAlarmRecordEvent);//抄送通知
    }

    private void assignAlarmNotify(AssignAlarmRecordEvent assignAlarmRecordEvent) {
        //待接收直接指派
        if (AlarmStatusEnum.ALARM_UN_CONFIRMED == assignAlarmRecordEvent.getPreviousAlarmStatus()) {
            //发送待办变更消息
            AlarmTodoRocketMQ.sendAlarmStatusChangeTodo(assignAlarmRecordEvent.getAlarmRecord(),
                                                        SecurityContextHolder.getPrincipal().getId(),
                                                        DateUtils.now(),
                                                        assignAlarmRecordEvent.getPreviousAlarmStatus());
            AlarmWorkCenterRocketMQ.sendAlarmStatusChangeTask(assignAlarmRecordEvent.getAlarmRecord(),
                                                              SecurityContextHolder.getPrincipal().getId(),
                                                              DateUtils.now(),
                                                              assignAlarmRecordEvent.getPreviousAlarmStatus());
            //发送指派通知
            this.alarmMessageSender.alarmAssignSend(assignAlarmRecordEvent.getAlarmRecord(),
                                                    assignAlarmRecordEvent.getAlarmRecord().getAlarmHandlerUsers()
                                                            .stream().map(AlarmHandlerUser::getId)
                                                            .collect(Collectors.toSet()));
        } else {
            List<Long> todoUserIds = new ArrayList<>();
            //原先存在处理人
            if (!CollectionUtils.isEmpty(assignAlarmRecordEvent.getAlarmHandlerUsers())) {
                //获取原先处理人id
                Set<Long> userIds = assignAlarmRecordEvent.getAlarmHandlerUsers().stream().map(AlarmHandlerUser::getId)
                        .collect(Collectors.toSet());
                //不包含再原先处理人中的用户
                todoUserIds = assignAlarmRecordEvent.getAssignAlarmRecordCommand().getAssignUsers().stream()
                        .map(AssignAlarmRecordUserCommand::getUserId)
                        .filter(userId -> !userIds.contains(userId))
                        .collect(Collectors.toList());
            }
            if (!CollectionUtils.isEmpty(todoUserIds)) {
                //发送待办初始化消息
                AlarmTodoRocketMQ.sendAlarmInitTodo(assignAlarmRecordEvent.getAlarmRecord(), todoUserIds);
                AlarmWorkCenterRocketMQ.sendAlarmInitWork(assignAlarmRecordEvent.getAlarmRecord(), todoUserIds);
                //发送指派通知
                this.alarmMessageSender.alarmAssignSend(assignAlarmRecordEvent.getAlarmRecord(),
                                                        new HashSet<>(todoUserIds));
            }
        }
    }

    private void assignAlarmCcNotify(AssignAlarmRecordEvent assignAlarmRecordEvent) {
        if (CollectionUtils.isEmpty(assignAlarmRecordEvent.getAssignAlarmRecordCommand().getCcMessageNotifies())) {
            return;
        }
        try {
            AlarmRecordPO alarmRecordPO = alarmRecordQueryService.getById(assignAlarmRecordEvent.getAlarmRecord().getAlarmInfo().getId());
            EventMessageSendParam param = new EventMessageSendParam();
            param.setEventId(Long.valueOf(alarmRecordPO.getBusinessKey()));
            param.setBusinessPrincipal(alarmRecordPO);
            param.setMessageNotifies(assignAlarmRecordEvent.getAssignAlarmRecordCommand().getCcMessageNotifies());
            linkageMessageClient.sendLinkageMessage(param);
        } catch (Exception e) {
            log.error("报警指派，抄送消息发送失败:", e);
        }
    }
}
