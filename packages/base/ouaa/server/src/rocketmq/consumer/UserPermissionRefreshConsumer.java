package com.guhecloud.cube.biz.ouaa.server.rocketmq.consumer;

import com.guhecloud.cube.base.message.sdk.SiteMessageSendClient;
import com.guhecloud.cube.base.message.sdk.api.request.SiteSendRequest;
import com.guhecloud.cube.biz.ouaa.server.entity.UserTenant;
import com.guhecloud.cube.biz.ouaa.server.permission.cache.UserPermissionCache;
import com.guhecloud.cube.biz.ouaa.server.rocketmq.message.UserPermissionRefreshMessage;
import com.guhecloud.cube.biz.ouaa.server.service.TerminalService;
import com.guhecloud.cube.biz.ouaa.server.service.UserTenantService;
import com.guhecloud.platform.base.context.SessionContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RocketMQMessageListener(consumerGroup = UserPermissionRefreshMessage.TOPIC, topic = UserPermissionRefreshMessage.TOPIC,
                         messageModel = MessageModel.CLUSTERING
)
public class UserPermissionRefreshConsumer implements RocketMQListener<UserPermissionRefreshMessage> {

    private final UserPermissionCache userPermissionCache;

    private final TerminalService terminalService;

    private final SiteMessageSendClient siteMessageSendClient;

    private final UserTenantService userTenantService;

    @Override
    public void onMessage(UserPermissionRefreshMessage message) {
        //刷新用户权限缓存
        userPermissionCache.cacheUserPermission(message.getUserId());
        sendPermissionRefreshMsg(message);
    }

    private void sendPermissionRefreshMsg(UserPermissionRefreshMessage message) {
        //通知的终端
        List<String> terminals = terminalService.getTenantTerminal();
        //过滤掉租户管理员用户
        UserTenant userTenantAdmin = this.userTenantService.getUserTenantAdmin(SessionContextHolder.getRequester().getTenant());
        Set<Long> user = message.getUserId().stream().filter(userId -> !userId.equals(userTenantAdmin.getUserId()))
                .collect(Collectors.toSet());
        //权限刷新消息发送
        SiteSendRequest siteSendRequest = SiteSendRequest.builder()
                .userIds(user)
                .eventType("AUTHORITY_REFRESH_MESSAGE")//权限刷新消息
                .msgType("AUTHORITY_REFRESH")
                .content("USER_AUTHORITY_REFRESH")
                .terminals(new HashSet<>(terminals))
                .build();
        this.siteMessageSendClient.send(siteSendRequest);
    }
}
