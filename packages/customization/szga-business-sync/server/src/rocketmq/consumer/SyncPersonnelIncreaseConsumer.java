package com.guhecloud.cube.srv.gen.business.info.sync.server.rocketmq.consumer;

import com.guhecloud.cube.srv.gen.business.info.sync.server.enumeration.Gender;
import com.guhecloud.cube.srv.gen.business.info.sync.server.handler.SyncUserInfoOperationHandler;
import com.guhecloud.cube.srv.gen.business.info.sync.server.rocketmq.message.user.IncreaseStaffMessage;
import com.guhecloud.cube.srv.gen.business.info.sync.server.rocketmq.message.user.IncreaseUserMessage;
import com.guhecloud.platform.base.context.SessionContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.guhecloud.platform.base.util.StringUtils;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2024/12/17 20:50
 * @Description: 人事管理新增
 */

@Slf4j
@Component
@RocketMQMessageListener(
        consumerGroup = IncreaseStaffMessage.TOPIC + "_INFO_SYNC",
        topic = IncreaseStaffMessage.TOPIC
)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SyncPersonnelIncreaseConsumer implements RocketMQListener<IncreaseStaffMessage> {
    private final SyncUserInfoOperationHandler userInfoOperationHandler;

    @Override
    public void onMessage(IncreaseStaffMessage message) {
        try {
            SessionContextHolder.requestTenant(message.getTenant());
            userInfoOperationHandler.increaseUser(IncreaseUserMessage.builder()
                                                          .id(message.getEmployeeId())
                                                          .name(message.getName())
                                                          .mobile(message.getMobile())
                                                          .organizationId(message.getOrganizationId())
                                                          .status(message.getStatus())
                                                          .idCard(message.getIdCard())
                                                          .gender(StringUtils.isNotBlank(message.getGender())
                                                                  ? Gender.valueOf(message.getGender()) : null)
                                                          .cornet(message.getCornet())
                                                          .build());
        } catch (Exception exception) {
            log.error("处理人事管理新增消息失败", exception);
            SessionContextHolder.clearSessionContext();
        }
    }
}
