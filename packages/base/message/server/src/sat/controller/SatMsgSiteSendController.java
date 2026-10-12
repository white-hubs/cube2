package com.guhecloud.cube.base.message2.server.sat.controller;

import com.guhecloud.cube.base.message2.server.handler.handler.send.site.dto.EventDataDTO;
import com.guhecloud.cube.base.message2.server.handler.handler.send.site.dto.MsgEventDataDTO;
import com.guhecloud.cube.base.message2.server.handler.handler.send.site.enumeration.SiteMsgTypeEnum;
import com.guhecloud.cube.base.message2.server.handler.handler.send.site.rocketmq.message.WebSocketSendMessage;
import com.guhecloud.cube.base.message2.server.handler.handler.send.site.rocketmq.message.WebSocketSendUser;
import com.guhecloud.cube.base.message2.server.sat.param.SatMsgSiteSendParam;
import com.guhecloud.cube.base.message2.server.sat.param.SatSiteBroadcastSendParam;
import com.guhecloud.cube.base.message2.server.sat.process.pipeline.domain.MessageRecordInfo;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.rocketmq.context.RocketMQContextHolder;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 平台站内信发送接口
 *
 * @author lanchun
 * @date 2022/6/6 18:15
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SatMsgSiteSendController {

    /**
     * 站内信发送
     *
     * @param param
     *
     * @return
     */
    @PostMapping("/sat-msg-site/send")
    public Boolean sendSite(@Valid @RequestBody SatMsgSiteSendParam param) {
        try {
            EventDataDTO eventDataDTO = new EventDataDTO(param.getEventDataParam().getEvent(), param.getEventDataParam().getData());
            //消息体
            MsgEventDataDTO msgEventDataDTO = new MsgEventDataDTO(param.getMsgType(), "terminal_tenant_person", eventDataDTO);
            List<WebSocketSendUser> webSocketSendUsers = param.getTargetModifyParams().stream()
                    .map(satMsgSiteTargetModifyParam -> new WebSocketSendUser(satMsgSiteTargetModifyParam.getUserId(),
                                                                              satMsgSiteTargetModifyParam.getTerminal()))
                    .collect(Collectors.toList());
            WebSocketSendMessage webSocketSendMessage = new WebSocketSendMessage(msgEventDataDTO, webSocketSendUsers,
                                                                                 SessionContextHolder.getRequester().getTenant(),
                                                                                 Collections.emptySet());
            RocketMQContextHolder.send(WebSocketSendMessage.TOPIC, webSocketSendMessage);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    /**
     * 站内广播发送
     *
     * @param param
     *
     * @return
     */
    @PostMapping("/sat-msg-site/broadcast/send")
    public Boolean sendBroadcastSite(@Valid @RequestBody SatSiteBroadcastSendParam param) {
        String msgType = SiteMsgTypeEnum.NOTICE_MESSAGE.toString();
        if (StringUtils.isNotBlank(param.getMsgType())) {
            msgType = param.getMsgType();
        }
        //消息体
        MsgEventDataDTO msgEventDataDTO = new MsgEventDataDTO(msgType, "broadcast",
                                                              new EventDataDTO(param.getEventType(),
                                                                               MessageRecordInfo.builder()
                                                                                       .msgContent(param.getContent())
                                                                                       .msgType(param.getEventType())
                                                                                       .businessPrincipal(
                                                                                               Objects.nonNull(param.getBusinessPrincipal())
                                                                                               ? JsonUtils.serialize(param.getBusinessPrincipal())
                                                                                               :
                                                                                               null)
                                                                                       .build()));
        WebSocketSendMessage webSocketSendMessage = new WebSocketSendMessage(msgEventDataDTO, Collections.emptyList(),
                                                                             Objects.nonNull(param.getTenant()) ? param.getTenant()
                                                                                                                : SessionContextHolder
                                                                                     .getRequester().getTenant(),
                                                                             param.getNonMatchTerminals());
        RocketMQContextHolder.send(WebSocketSendMessage.TOPIC, webSocketSendMessage);
        return Boolean.TRUE;
    }

    /**
     * 站内广播发送
     *
     * @param param
     *
     * @return
     */
    @PostMapping("/sat-msg-site/customer/broadcast/send")
    public Boolean sendCustomerBroadcastSite(@Valid @RequestBody SatSiteBroadcastSendParam param) {
        String msgType = SiteMsgTypeEnum.NOTICE_MESSAGE.toString();
        if (StringUtils.isNotBlank(param.getMsgType())) {
            msgType = param.getMsgType();
        }
        List<WebSocketSendUser> webSocketSendUsers = new ArrayList<>();
        param.getUserIds().forEach(userId -> {
            param.getTerminals().forEach(terminal -> {
                webSocketSendUsers.add(new WebSocketSendUser(userId,
                                                             terminal));
            });
        });
        MsgEventDataDTO msgEventDataDTO = new MsgEventDataDTO(msgType, "broadcast_user",
                                                              new EventDataDTO(param.getEventType(), param.getContent()));
        WebSocketSendMessage webSocketSendMessage = new WebSocketSendMessage(msgEventDataDTO, webSocketSendUsers,
                                                                             Objects.nonNull(param.getTenant()) ? param.getTenant()
                                                                                                                : SessionContextHolder
                                                                                     .getRequester().getTenant(),
                                                                             Collections.emptySet());
        RocketMQContextHolder.send(WebSocketSendMessage.TOPIC, webSocketSendMessage);
        return Boolean.TRUE;
    }

    /**
     * 终端用户站内发送
     *
     * @param param
     *
     * @return
     */
    @PostMapping("/sat-msg-site/terminal-user/send")
    public Boolean sendByTerminal(@Valid @RequestBody SatMsgSiteSendParam param) {
        try {
            EventDataDTO eventDataDTO = new EventDataDTO(param.getEventDataParam().getEvent(), param.getEventDataParam().getData());
            //消息体
            MsgEventDataDTO msgEventDataDTO = new MsgEventDataDTO(param.getMsgType(), "terminal_user", eventDataDTO);
            List<WebSocketSendUser> webSocketSendUsers = param.getTargetModifyParams().stream()
                    .map(satMsgSiteTargetModifyParam -> new WebSocketSendUser(satMsgSiteTargetModifyParam.getUserId(),
                                                                              satMsgSiteTargetModifyParam.getTerminal()))
                    .collect(Collectors.toList());
            WebSocketSendMessage webSocketSendMessage = new WebSocketSendMessage(msgEventDataDTO, webSocketSendUsers,
                                                                                 null, Collections.emptySet());
            RocketMQContextHolder.send(WebSocketSendMessage.TOPIC, webSocketSendMessage);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }
}
