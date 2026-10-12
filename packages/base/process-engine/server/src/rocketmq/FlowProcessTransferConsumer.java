package com.guhecloud.cube.base.flow.rocketmq;

import com.guhecloud.cube.base.flow.flowable.task.application.commandgatway.ActivatedTaskCommandService;
import com.guhecloud.cube.base.flow.flowable.task.application.commandgatway.command.TransferCommand;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.work.center.client.message.WorkTaskTransferMessage;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import com.guhecloud.platform.security.model.Principal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/11/27 15:10
 * @Description:
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RocketMQMessageListener(consumerGroup = WorkTaskTransferMessage.TOPIC + "_APPROVE_TASK",
                         topic = WorkTaskTransferMessage.TOPIC,
                         selectorExpression = "APPROVE_TASK"
)
public class FlowProcessTransferConsumer implements RocketMQListener<WorkTaskTransferMessage> {
    private final ActivatedTaskCommandService activatedTaskCommandService;
    private final UserClient userClient;

    @Override
    public void onMessage(WorkTaskTransferMessage message) {
        try {
            var user = userClient.getUser(message.getUserId());
            if (user == null) {
                log.error("user not found: {}", message.getUserId());
                return;
            }
            var principal = Principal.builder().id(message.getUserId()).account(user.getUsername()).name(user.getName())
                    .type(Principal.Type.USER)
                    .build();
            SecurityContextHolder.setPrincipal(principal);
            SessionContextHolder.requestTenant(message.getTenantId());
            TransferCommand transferCommand = new TransferCommand();
            transferCommand.setAccount(String.valueOf(message.getUserId()));
            transferCommand.setTargetAccount(String.valueOf(message.getTargetId()));
            for (String businessKey : message.getBusinessKeyList()) {

                transferCommand.setTaskId(businessKey);
                activatedTaskCommandService.transfer(transferCommand);
            }
        } catch (Exception e) {
            log.error(" daily task transfer error: {}", e.getMessage(), e);
        }

    }
}
