package com.guhecloud.cube.srv.gen.maintain.server.electron.event;

import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.work.center.client.message.WorkTaskTransferMessage;
import com.guhecloud.cube.srv.gen.maintain.server.electron.service.PatrolTaskService;
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

/**
 * 巡更任务交接监听
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RocketMQMessageListener(consumerGroup = WorkTaskTransferMessage.TOPIC + "_PATROL",
                         topic = WorkTaskTransferMessage.TOPIC,
                         selectorExpression = "ELECTRONIC_PATROL"
)
public class PatrolTaskTransferConsumer implements RocketMQListener<WorkTaskTransferMessage> {
    private final UserClient userClient;
    private final PatrolTaskService patrolTaskService;

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
        message.getBusinessKeyList()
                .forEach(businessKey -> patrolTaskService.turnOverTask(Long.valueOf(businessKey), message.getTargetId()));
        SecurityContextHolder.clearSecurityContext();
        SessionContextHolder.clearSessionContext();
    }
}
