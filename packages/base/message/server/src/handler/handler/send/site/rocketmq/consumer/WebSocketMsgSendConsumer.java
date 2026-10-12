package com.guhecloud.cube.base.message2.server.handler.handler.send.site.rocketmq.consumer;

import com.guhecloud.cube.base.message2.server.handler.handler.send.site.dto.ConsumerWebsocketDTO;
import com.guhecloud.cube.base.message2.server.handler.handler.send.site.handler.MsgWebsocketHandler;
import com.guhecloud.cube.base.message2.server.handler.handler.send.site.rocketmq.message.WebSocketSendMessage;
import lombok.RequiredArgsConstructor;
import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.annotation.SelectorType;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

/**
 * webSocket消息发送消费
 */
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RocketMQMessageListener(consumerGroup = "web_site_socket_send", topic = WebSocketSendMessage.TOPIC, selectorType = SelectorType.TAG,
                         messageModel = MessageModel.BROADCASTING
)
public class WebSocketMsgSendConsumer implements RocketMQListener<WebSocketSendMessage> {

    private final MsgWebsocketHandler msgWebsocketHandler;

    @Override
    public void onMessage(WebSocketSendMessage message) {
        if ("broadcast".equals(message.getMsgEventDataDTO().getSendType())) {
            //广播站内消息
            msgWebsocketHandler.broadcastSend(message.getMsgEventDataDTO(), message.getTenant(), message.getNonMatchTerminals());
        } else if ("broadcast_user".equals(message.getMsgEventDataDTO().getSendType())) {
            //按终端租户用户发送
            message.getWebSocketSendUsers().forEach(targetParam -> {
                String sendUser = message.getTenant() + ":" + targetParam.getUserId();
                msgWebsocketHandler.broadcastUserSend(message.getMsgEventDataDTO(), sendUser, targetParam.getTerminal());
            });
        } else if ("terminal_tenant_person".equals(message.getMsgEventDataDTO().getSendType())) {
            //按终端租户用户发送
            message.getWebSocketSendUsers().forEach(targetParam -> {
                String sendUser = message.getTenant() + ":" + targetParam.getUserId();
                List<ConsumerWebsocketDTO> userConsumerWebsocketDTOS = msgWebsocketHandler.getTerminalTenantOnLineUser(sendUser, targetParam.getTerminal());
                userConsumerWebsocketDTOS.forEach(consumerWebsocketDTO -> {
                    //当前实例存在该用户才发送消息
                    if (Objects.nonNull(consumerWebsocketDTO) &&
                        !"broadcast".equals(consumerWebsocketDTO.getSubscribeType())) {
                        msgWebsocketHandler.send(consumerWebsocketDTO.getSessionId(), message.getMsgEventDataDTO());
                    }
                });
            });
        } else if ("terminal_user".equals(message.getMsgEventDataDTO().getSendType())) {
            //按终端用户发送
            message.getWebSocketSendUsers().forEach(targetParam -> {
                String sendUser = targetParam.getTerminal() + ":" + targetParam.getUserId();
                List<ConsumerWebsocketDTO> terminalOnLineUser = msgWebsocketHandler.getTerminalOnLineUser(sendUser);
                terminalOnLineUser.forEach(consumerWebsocketDTO -> {
                    //当前实例存在该用户才发送消息
                    if (Objects.nonNull(consumerWebsocketDTO)) {
                        msgWebsocketHandler.send(consumerWebsocketDTO.getSessionId(), message.getMsgEventDataDTO());
                    }
                });
            });
        }
    }
}

