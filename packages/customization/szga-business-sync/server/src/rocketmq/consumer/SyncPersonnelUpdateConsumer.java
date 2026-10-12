package com.guhecloud.cube.srv.gen.business.info.sync.server.rocketmq.consumer;

import com.guhecloud.cube.srv.gen.business.info.sync.server.handler.SyncUserInfoOperationHandler;
import com.guhecloud.cube.srv.gen.business.info.sync.server.rocketmq.message.user.UpdateStaffMessage;
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
 * @Date: 2024/12/17 20:12
 * @Description: 人事管理数据更新监听
 */

@Slf4j
@Component
@RocketMQMessageListener(
        consumerGroup = UpdateStaffMessage.TOPIC + "_INFO_SYNC",
        topic = UpdateStaffMessage.TOPIC
)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SyncPersonnelUpdateConsumer implements RocketMQListener<UpdateStaffMessage> {
    private final SyncUserInfoOperationHandler userInfoOperationHandler;

    @Override
    public void onMessage(UpdateStaffMessage message) {
        try {
            SessionContextHolder.requestTenant(message.getTenant());
            userInfoOperationHandler.updateUser(UserUpdateMessage.builder()
                                                        .id(message.getUserId())
                                                        .name(message.getName())
                                                        .mobile(message.getMobile())
                                                        .organizationId(message.getOrganizationId())
                                                        .status(message.getStatus())
                                                        .cornet(message.getCornet())
                                                        .idCard(message.getIdCard())
                                                        .build());
        } catch (Exception exception) {
            log.error("处理人事管理数据更新消息失败", exception);
            SessionContextHolder.clearSessionContext();
        }
    }
}
