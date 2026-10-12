package com.guhecloud.cube.base.alarm.server.flow.infrastructure.mq.rocketmq.consumer;

import com.guhecloud.cube.base.alarm.server.flow.application.commandgateway.command.TransferAlarmRecordCommand;
import com.guhecloud.cube.base.alarm.server.state.AlarmStateContext;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.work.center.client.message.WorkTaskTransferMessage;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import com.guhecloud.platform.security.model.Principal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 报警任务(记录)交接监听
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RocketMQMessageListener(consumerGroup = WorkTaskTransferMessage.TOPIC + "_ALARM",
                         topic = WorkTaskTransferMessage.TOPIC,
                         selectorExpression = "ALARM_TASK"
)
public class AlarmTaskTransferConsumer implements RocketMQListener<WorkTaskTransferMessage> {
    private final AlarmStateContext alarmStateContext;
    private final UserClient userClient;

    @Override
    public void onMessage(WorkTaskTransferMessage message) {
        log.info("receive inspect task transfer message: {}", message);
        if (message == null || CollectionUtils.isEmpty(message.getBusinessKeyList()) || message.getTargetId() == null) {
            return;
        }
        var user = userClient.getUser(message.getUserId());
        if (user == null) {
            log.error("user not found: {}", message.getUserId());
            return;
        }
        var principal = Principal.builder().id(message.getUserId()).account(user.getUsername()).name(user.getName())
                .type(Principal.Type.USER)
                .build();
        SessionContextHolder.requestTenant(message.getTenantId());
        SecurityContextHolder.setPrincipal(principal);

        var targetUser = userClient.getUser(message.getTargetId());
        List<TransferAlarmRecordCommand> params = message.getBusinessKeyList()
                .stream()
                .map(businessKey -> {
                    TransferAlarmRecordCommand param = new TransferAlarmRecordCommand();
                    param.setUserId(message.getTargetId());
                    param.setUserName(targetUser.getUsername());
                    param.setDepartmentId(targetUser.getOrganization().getId());
                    param.setDepartmentName(targetUser.getOrganization().getName());
                    param.setId(Long.valueOf(businessKey));
                    return param;
                }).toList();

        alarmStateContext.transfersAlarmRecord(params);
        SecurityContextHolder.clearSecurityContext();
        SessionContextHolder.clearSessionContext();
    }
}
