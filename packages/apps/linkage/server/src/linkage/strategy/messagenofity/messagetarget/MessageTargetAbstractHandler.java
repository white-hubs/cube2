package com.guhecloud.cube.biz.linkage.server.linkage.strategy.messagenofity.messagetarget;

import com.guhecloud.apps.linkage.metadata.dto.MessageTargetNotifyDTO;
import com.guhecloud.apps.linkage.metadata.enumeration.MessageTargetNotifyEnum;

public abstract class MessageTargetAbstractHandler implements MessageTargetHandler {

    private MessageTargetNotifyEnum messageTargetNotify;

    public MessageTargetAbstractHandler(MessageTargetNotifyEnum messageTargetNotify) {
        this.messageTargetNotify = messageTargetNotify;
    }

    @Override
    public boolean supports(MessageTargetNotifyDTO messageTargetNotifyDTO) {
        return this.messageTargetNotify == messageTargetNotifyDTO.getMessageTargetNotify();
    }
}
