package com.guhecloud.cube.srv.gen.business.info.sync.server.rocketmq.consumer;

import com.guhecloud.cube.srv.gen.business.info.sync.server.handler.SyncUserInfoOperationHandler;
import com.guhecloud.cube.srv.gen.business.info.sync.server.rocketmq.message.user.IncreaseUserMessage;
import com.guhecloud.platform.base.context.SessionContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2024/12/17 16:37
 * @Description: 增加用户消息消费者
 */

@Slf4j
@Component
@RocketMQMessageListener(
        consumerGroup = IncreaseUserMessage.TOPIC + "_INFO_SYNC",
        topic = IncreaseUserMessage.TOPIC
)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SyncIncreaseUserConsumer implements RocketMQListener<IncreaseUserMessage> {
    private final SyncUserInfoOperationHandler userInfoOperationHandler;

    @Override
    public void onMessage(IncreaseUserMessage message) {
        log.info("接收到增加用户消息:{}", message);
        try {
            // 调用新增员工
            SessionContextHolder.requestTenant(message.getTenant());
            userInfoOperationHandler.increaseUser(message);
        } catch (Exception exception) {
            log.error("处理增加用户消息失败", exception);
            SessionContextHolder.clearSessionContext();
        }
    }
}
