package com.guhecloud.cube.biz.ouaa.server.rocketmq.consumer;

import com.guhecloud.cube.base.message.sdk.MessageSendClient;
import com.guhecloud.cube.base.message.sdk.api.enumeration.MessageNotifyTargetEnum;
import com.guhecloud.cube.base.message.sdk.api.model.MessageNotifyTarget;
import com.guhecloud.cube.base.message.sdk.api.request.MessageSceneSendRequest;
import com.guhecloud.cube.biz.ouaa.server.entity.Tenant;
import com.guhecloud.cube.biz.ouaa.server.entity.UserTenant;
import com.guhecloud.cube.biz.ouaa.server.rocketmq.message.AppVersionUpgradeMessage;
import com.guhecloud.cube.biz.ouaa.server.service.TenantService;
import com.guhecloud.cube.biz.ouaa.server.service.UserTenantService;
import com.guhecloud.platform.base.context.SessionContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RocketMQMessageListener(consumerGroup = "APP_VERSION_GROUP", topic = AppVersionUpgradeMessage.TOPIC,
                         messageModel = MessageModel.CLUSTERING
)
public class AppVersionUpgradeConsumer implements RocketMQListener<AppVersionUpgradeMessage> {

    private final TenantService tenantService;

    private final UserTenantService userTenantService;

    private final MessageSendClient messageSendClient;

    private static final String APP_VERSION_MSG_SCENE = "APP_VERSION_UPGRADE_NOTICE";

    @Override
    public void onMessage(AppVersionUpgradeMessage message) {
        List<Tenant> tenants = this.tenantService.list();
        tenants.stream().forEach(tenant -> {
            try {
                SessionContextHolder.requestTenant(tenant.getCode());
                Set<Long> userIds = this.userTenantService.listUserTenants(tenant.getCode())
                        .stream()
                        .map(UserTenant::getUserId)
                        .collect(Collectors.toSet());
                messageSendClient.sceneSend(MessageSceneSendRequest.builder()
                                                    .msgSceneCode(APP_VERSION_MSG_SCENE)
                                                    .tenant(tenant.getCode())
                                                    .businessId(message.getId().toString())
                                                    .messageNotifyTargets(Collections.singletonList(MessageNotifyTarget.builder()
                                                                                                            .notifyTargetType(MessageNotifyTargetEnum.USER)
                                                                                                            .ids(userIds)
                                                                                                            .build()))
                                                    .build());
                SessionContextHolder.clearSessionContext();
            } catch (Exception e) {
                log.error("app版本升级租户编号为:{}的消息发送失败,失败原因,{}", tenant.getCode(), e);
            }
        });
    }
}
