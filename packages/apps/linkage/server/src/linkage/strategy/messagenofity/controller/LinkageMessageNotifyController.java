package com.guhecloud.cube.biz.linkage.server.linkage.strategy.messagenofity.controller;

import com.guhecloud.apps.linkage.metadata.dto.MessageTargetNotifyDTO;
import com.guhecloud.apps.linkage.metadata.enumeration.MessageNotifyTypeEnum;
import com.guhecloud.apps.linkage.metadata.param.EventMessageSendParam;
import com.guhecloud.cube.base.message.sdk.api.model.MessageNotifyTarget;
import com.guhecloud.cube.biz.linkage.server.linkage.entity.Event;
import com.guhecloud.cube.biz.linkage.server.linkage.service.EventService;
import com.guhecloud.cube.biz.linkage.server.linkage.strategy.messagenofity.dto.LinkageMessageDTO;
import com.guhecloud.cube.biz.linkage.server.linkage.strategy.messagenofity.handler.LinkageMessageNotifyHandler;
import com.guhecloud.cube.biz.linkage.server.linkage.strategy.messagenofity.messagenotify.handler.MessageImmediatelyNotifyHandler;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class LinkageMessageNotifyController {

    private final LinkageMessageNotifyHandler linkageMessageNotifyHandler;

    private final MessageImmediatelyNotifyHandler linkageImmediatelyNotifyHandler;

    private final EventService eventService;

    /**
     * 联动消息立即通知
     *
     * @param eventMessageSendParam 发送参数
     */
    @PostMapping("/linage/message/immediately/notify")
    public void sendLinkageMessage(@RequestBody @Valid EventMessageSendParam eventMessageSendParam) {
        Event event = this.eventService.getById(eventMessageSendParam.getEventId());
        Assert.isNotFound(event, "报警事件不存在");
        List<LinkageMessageDTO> linkageMessageDTOS = eventMessageSendParam.getMessageNotifies()
                .stream().map(notifyConfig -> {
                    LinkageMessageDTO linkageMessageDTO = new LinkageMessageDTO();
                    linkageMessageDTO.setNotifyType(MessageNotifyTypeEnum.IMMEDIATELY_NOTIFY);
                    linkageMessageDTO.setChannelTypes(notifyConfig.getChannelTypes());
                    linkageMessageDTO.setTargetModifyParams(notifyConfig.getTargetModifyParams());
                    return linkageMessageDTO;
                }).toList();
        linkageMessageNotifyHandler.sendLinkageMessage(event, eventMessageSendParam.getBusinessPrincipal(), linkageMessageDTOS);
    }

    /**
     * 构造消息发送人信息
     *
     * @param targetNotifies 对象通知配置
     *
     * @return 消息发送人信息
     */
    @PostMapping("/linage/message/notify/targets")
    public List<MessageNotifyTarget> buildMessageNotifyTargets(@RequestBody List<MessageTargetNotifyDTO> targetNotifies) {
        if (CollectionUtils.isEmpty(targetNotifies)) {
            return Collections.emptyList();
        }
        return linkageImmediatelyNotifyHandler.buildNotifyTarget(targetNotifies);
    }
}
