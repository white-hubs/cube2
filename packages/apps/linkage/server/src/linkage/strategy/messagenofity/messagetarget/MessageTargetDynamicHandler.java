package com.guhecloud.cube.biz.linkage.server.linkage.strategy.messagenofity.messagetarget;

import com.guhecloud.apps.linkage.metadata.dto.MessageTargetNotifyDTO;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.platform.base.standard.exception.ResourceNotFoundException;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class MessageTargetDynamicHandler implements ApplicationContextAware, MessageTargetHandler {

    private List<MessageTargetHandler> messageTargetHandlers = new ArrayList<>();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        applicationContext.getBeansOfType(MessageTargetHandler.class).forEach((k, v) -> messageTargetHandlers.add(v));
    }

    @Override
    public boolean supports(MessageTargetNotifyDTO messageTargetNotifyDTO) {
        return false;
    }

    @Override
    public List<UserDTO> getUsers(MessageTargetNotifyDTO messageTargetNotifyDTO) {
        return this.determineMessageTargetHandler(messageTargetNotifyDTO).getUsers(messageTargetNotifyDTO);
    }

    @Override
    public Set<Long> getTargetIds(MessageTargetNotifyDTO messageTargetNotifyDTO) {
        return this.determineMessageTargetHandler(messageTargetNotifyDTO).getTargetIds(messageTargetNotifyDTO);
    }

    private MessageTargetHandler determineMessageTargetHandler(MessageTargetNotifyDTO messageTargetNotifyDTO) {
        return this.messageTargetHandlers.stream().filter(messageTargetHandler -> messageTargetHandler.supports(messageTargetNotifyDTO))
                .findFirst().orElseThrow(() -> new ResourceNotFoundException(
                        "没有找到 input=" + messageTargetNotifyDTO.getMessageTargetNotify() + " 的策略处理器"));
    }
}
