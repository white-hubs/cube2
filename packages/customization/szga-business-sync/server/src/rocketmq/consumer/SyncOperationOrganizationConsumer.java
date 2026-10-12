package com.guhecloud.cube.srv.gen.business.info.sync.server.rocketmq.consumer;

import com.guhecloud.cube.srv.gen.business.info.sync.server.handler.SyncOrganizationInfoOperationHandler;
import com.guhecloud.cube.srv.gen.business.info.sync.server.rocketmq.message.organization.OperationOrganizationMessage;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.cloud.logger.constant.OperationType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2024/12/18 10:04
 * @Description: 组织关系更新操作
 */

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RocketMQMessageListener(consumerGroup = OperationOrganizationMessage.TOPIC + "_" + "INFO_SYNC",
                         topic = OperationOrganizationMessage.TOPIC
)
public class SyncOperationOrganizationConsumer implements RocketMQListener<OperationOrganizationMessage> {
    private final SyncOrganizationInfoOperationHandler organizationInfoOperationHandler;

    @Override
    public void onMessage(OperationOrganizationMessage message) {
        try {
            SessionContextHolder.requestTenant(message.getTenant());
            switch (message.getOperationType()) {
                case OperationType.ADD:
                    organizationInfoOperationHandler.insert(message);
                    break;
                case OperationType.UPDATE:
                    organizationInfoOperationHandler.update(message);
                    break;
                case OperationType.DELETE:
                    organizationInfoOperationHandler.delete(message);
                    break;
                default:
                    log.error("未知操作类型: {}", message.getOperationType());
            }
        } catch (Exception exception) {
            log.error("处理组织关系更新消息失败", exception);
            SessionContextHolder.clearSessionContext();
        }
    }
}
