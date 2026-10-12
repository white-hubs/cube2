package com.guhecloud.cube.srv.gen.safety.personnel.server.sync.consumer;

import com.guhecloud.cube.biz.ouaa.client.message.UserUpdateMessage;
import com.guhecloud.cube.srv.gen.safety.personnel.server.sync.service.SafetyPersonnelUserStatusSyncService;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.JsonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

/**
 * OUAA用户更新消息消费者。
 */
@Slf4j
@Component("safetyPersonnelUserUpdateConsumer")
@RequiredArgsConstructor
@RocketMQMessageListener(consumerGroup = UserUpdateMessage.TOPIC + "_SAFETY_PERSONNEL", topic = UserUpdateMessage.TOPIC)
public class SafetyPersonnelUserUpdateConsumer implements RocketMQListener<UserUpdateMessage> {

    private final SafetyPersonnelUserStatusSyncService userStatusSyncService;

    @Override
    public void onMessage(UserUpdateMessage message) {
        if (message == null || message.getId() == null) {
            return;
        }
        try {
            if (message.getTenant() != null) {
                SessionContextHolder.requestTenant(message.getTenant());
            }
            userStatusSyncService.handleOuaaUserUpdate(message);
        } catch (Exception e) {
            log.error("安全人员消费OUAA用户更新消息失败，message={}", JsonUtils.serialize(message), e);
        } finally {
            SessionContextHolder.clearSessionContext();
        }
    }
}
