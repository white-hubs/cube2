package com.guhecloud.cube.srv.gen.business.info.sync.server.rocketmq.consumer;

import com.guhecloud.cube.srv.gen.business.info.sync.server.handler.SyncUserInfoOperationHandler;
import com.guhecloud.cube.srv.gen.business.info.sync.server.rocketmq.message.user.UserUpdateMessage;
import com.guhecloud.platform.base.context.SessionContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2024/12/17 17:36
 * @Description: 更新用户信息消费者
 */

@Slf4j
@Component
@RocketMQMessageListener(
        consumerGroup = UserUpdateMessage.TOPIC + "_INFO_SYNC",
        topic = UserUpdateMessage.TOPIC
)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SyncUpdateUserConsumer implements RocketMQListener<UserUpdateMessage> {
    private final SyncUserInfoOperationHandler userInfoOperationHandler;

    @Override
    public void onMessage(UserUpdateMessage message) {
        log.info("接收修改的用户下信息:{}", message);
        try {
            SessionContextHolder.requestTenant(message.getTenant());
            userInfoOperationHandler.updateUser(message);
        } catch (Exception exception) {
            log.error("处理修改用户信息失败", exception);
            SessionContextHolder.clearSessionContext();
        }
    }
}
