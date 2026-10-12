package com.guhecloud.cube.biz.linkage.server.linkage.strategy.messagenofity.messagenotify;

import com.guhecloud.apps.linkage.metadata.enumeration.MessageNotifyTypeEnum;
import com.guhecloud.cube.biz.linkage.server.linkage.entity.Event;
import com.guhecloud.cube.biz.linkage.server.linkage.strategy.messagenofity.dto.LinkageMessageDTO;

public interface MessageNotifyHandler {

    boolean supports(MessageNotifyTypeEnum messageNotifyType);

    void messageNotify(Event event, LinkageMessageDTO linkageMessageDTO, Object businessPrincipal);
}
