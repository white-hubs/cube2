package com.guhecloud.cube.biz.linkage.server.linkage.strategy.messagenofity.messagenotify;

import com.guhecloud.apps.linkage.metadata.enumeration.MessageNotifyTypeEnum;
import com.guhecloud.cube.biz.linkage.server.linkage.entity.Event;
import com.guhecloud.cube.biz.linkage.server.linkage.strategy.messagenofity.dto.LinkageMessageDTO;
import com.guhecloud.platform.base.standard.exception.ResourceNotFoundException;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MessageNotifyDynamicHandler implements ApplicationContextAware, MessageNotifyHandler {

    private List<MessageNotifyHandler> messageNotifyHandlers = new ArrayList<>();

    @Override
    public boolean supports(MessageNotifyTypeEnum messageNotifyType) {
        return false;
    }

    @Override
    public void messageNotify(Event event, LinkageMessageDTO linkageMessageDTO, Object businessPrincipal) {
        this.determineMessageTargetHandler(linkageMessageDTO.getNotifyType()).messageNotify(event, linkageMessageDTO, businessPrincipal);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        applicationContext.getBeansOfType(MessageNotifyHandler.class).forEach((k, v) -> messageNotifyHandlers.add(v));
    }

    private MessageNotifyHandler determineMessageTargetHandler(MessageNotifyTypeEnum messageNotifyTypeEnum) {
        return this.messageNotifyHandlers.stream().filter(messageNotifyHandler -> messageNotifyHandler.supports(messageNotifyTypeEnum))
                .findFirst().orElseThrow(() -> new ResourceNotFoundException("没有找到 input=" + messageNotifyTypeEnum + " 的策略处理器"));
    }
}
