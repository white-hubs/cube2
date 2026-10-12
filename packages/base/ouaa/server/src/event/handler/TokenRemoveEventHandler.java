package com.guhecloud.cube.biz.ouaa.server.event.handler;

import com.guhecloud.cube.base.message.sdk.SiteMessageSendClient;
import com.guhecloud.cube.base.message.sdk.api.request.SiteSendRequest;
import com.guhecloud.cube.biz.ouaa.server.entity.User;
import com.guhecloud.cube.biz.ouaa.server.service.UserService;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.redis.event.TokenRemoveEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
public class TokenRemoveEventHandler {

    private final SiteMessageSendClient siteMessageSendClient;

    private final UserService userService;

    public TokenRemoveEventHandler(SiteMessageSendClient siteMessageSendClient, UserService userService
                                  ) {
        this.siteMessageSendClient = siteMessageSendClient;
        this.userService = userService;
    }

    @EventListener
    public void TokenRemoveEvent(final TokenRemoveEvent event) {
        try {
            Set<String> jots = event.getRemoveTokens().stream()
                    .map(jtiKey ->
                                 jtiKey.substring(jtiKey.lastIndexOf(":") + 1)
                        ).collect(Collectors.toSet());
            User user = userService.lookupUser(event.getAccount());
            SiteSendRequest siteSendRequest = SiteSendRequest.builder()
                    .userIds(Collections.singleton(user.getId()))
                    .eventType("REMOTE_LOGIN_MESSAGE")
                    .msgType("REMOTE_LOGIN")//权限刷新消息
                    .content(JsonUtils.serialize(jots))
                    .terminals(Collections.singleton(event.getTerminal()))
                    .build();
            this.siteMessageSendClient.terminalUserSend(siteSendRequest);
        } catch (Exception e) {
            log.error("账号被异地登录后站内消息发送失败,失败原因:", e);
        }
    }
}
