package com.guhecloud.cube.base.message2.server.handler.handler.send.site;

import com.guhecloud.base.message.metadata.enums.ChannelEnum;
import com.guhecloud.cube.base.message2.server.handler.dto.MessageSendResponse;
import com.guhecloud.cube.base.message2.server.handler.handler.send.BaseMsgSender;
import com.guhecloud.cube.base.message2.server.handler.handler.send.site.dto.EventDataDTO;
import com.guhecloud.cube.base.message2.server.handler.handler.send.site.dto.MsgEventDataDTO;
import com.guhecloud.cube.base.message2.server.handler.handler.send.site.enumeration.SiteMsgTypeEnum;
import com.guhecloud.cube.base.message2.server.handler.handler.send.site.rocketmq.message.WebSocketSendMessage;
import com.guhecloud.cube.base.message2.server.handler.handler.send.site.rocketmq.message.WebSocketSendUser;
import com.guhecloud.cube.base.message2.server.handler.handler.tmp.site.dto.SiteTerminal;
import com.guhecloud.cube.base.message2.server.sat.process.pipeline.domain.MessageInfo;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.rocketmq.context.RocketMQContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 站内信发送实现
 *
 * @author lanchun
 * @date 2023/5/11 10:36
 */
@Slf4j
@Component
public class SiteSender extends BaseMsgSender {

    public SiteSender() {
        this.channelCode = ChannelEnum.CHANNEL_SITE;
    }

    @Override
    public MessageSendResponse doSend(MessageInfo messageInfo) {
        //消息体
        MsgEventDataDTO msgEventDataDTO = new MsgEventDataDTO(SiteMsgTypeEnum.NOTICE_MESSAGE.toString(),
                                                              "terminal_tenant_person",
                                                              new EventDataDTO(messageInfo.getMsgType(), messageInfo.getBusinessPrincipal()));
        //webSocket消息发送
        List<WebSocketSendUser> webSocketSendUsers = Collections.synchronizedList(new ArrayList<>());
        //拆分终端
        SiteTerminal siteTerminal = JsonUtils.deserialize(messageInfo.getTmpConfig(), SiteTerminal.class);
        siteTerminal.getTerminals().forEach(terminal ->
                                                    messageInfo.getReceiver().parallelStream().forEach(userId ->
                                                                                                               webSocketSendUsers.add(new WebSocketSendUser(Long.valueOf(userId), terminal))
                                                                                                      )
                                           );
        WebSocketSendMessage webSocketSendMessage = new WebSocketSendMessage(msgEventDataDTO, webSocketSendUsers, messageInfo.getTenant());
        RocketMQContextHolder.send(WebSocketSendMessage.TOPIC, webSocketSendMessage);
        return MessageSendResponse.builder()
                .id(messageInfo.getId())
                .succeed(true)
                .succeedReceiver(messageInfo.getReceiver())
                .build();
    }

    @Override
    public String verification(MessageInfo messageInfo) {
        return StringUtils.EMPTY_STRING;
    }
}
