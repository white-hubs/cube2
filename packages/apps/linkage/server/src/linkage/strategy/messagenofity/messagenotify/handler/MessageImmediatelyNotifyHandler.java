package com.guhecloud.cube.biz.linkage.server.linkage.strategy.messagenofity.messagenotify.handler;

import com.guhecloud.apps.linkage.metadata.enumeration.MessageNotifyTypeEnum;
import com.guhecloud.cube.biz.linkage.server.linkage.entity.Event;
import com.guhecloud.cube.biz.linkage.server.linkage.strategy.messagenofity.dto.LinkageMessageDTO;
import com.guhecloud.cube.biz.linkage.server.linkage.strategy.messagenofity.messagenotify.MessageNotifyAbstractHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 立即通知
 *
 * @author lanchun
 * @date 2022/11/2 11:22
 */
@Slf4j
@Component
public class MessageImmediatelyNotifyHandler extends MessageNotifyAbstractHandler {

    public MessageImmediatelyNotifyHandler() {
        super(MessageNotifyTypeEnum.IMMEDIATELY_NOTIFY);
    }

    @Override
    public void messageNotify(Event event, LinkageMessageDTO linkageMessageDTO, Object businessPrincipal) {
        this.sendMessage(linkageMessageDTO, event.getBusinessType(), String.valueOf(event.getId()), event.getEventConfig(), event.getApplicationCode(), event.getApplicationName(), businessPrincipal);
    }
}
