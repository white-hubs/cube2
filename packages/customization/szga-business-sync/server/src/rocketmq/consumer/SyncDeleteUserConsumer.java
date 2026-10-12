package com.guhecloud.cube.srv.gen.business.info.sync.server.rocketmq.consumer;

import com.guhecloud.cube.srv.gen.business.info.sync.server.handler.SyncUserInfoOperationHandler;
import com.guhecloud.cube.srv.gen.business.info.sync.server.rocketmq.message.user.DeleteUserMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2024/12/17 18:00
 * @Description: 删除用户消息消费者
 */

@Slf4j
@Component
@RocketMQMessageListener(
        consumerGroup = DeleteUserMessage.TOPIC + "_INFO_SYNC",
        topic = DeleteUserMessage.TOPIC
)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SyncDeleteUserConsumer implements RocketMQListener<DeleteUserMessage> {
    private final SyncUserInfoOperationHandler userInfoOperationHandler;

    @Override
    public void onMessage(DeleteUserMessage message) {
        userInfoOperationHandler.deleteUserMessage(message);
    }
}
