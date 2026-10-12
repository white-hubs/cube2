package com.guhecloud.cube.biz.linkage.server.linkage.strategy.messagenofity.messagetarget.handler;

import com.fasterxml.jackson.core.type.TypeReference;
import com.guhecloud.apps.linkage.metadata.dto.MessageTargetNotifyDTO;
import com.guhecloud.apps.linkage.metadata.enumeration.MessageTargetNotifyEnum;
import com.guhecloud.cube.biz.linkage.server.linkage.strategy.messagenofity.messagetarget.MessageTargetAbstractHandler;
import com.guhecloud.cube.biz.linkage.server.linkage.strategy.messagenofity.messagetarget.dto.MessagePostDTO;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
public class PostHandler extends MessageTargetAbstractHandler {

    @Autowired
    private UserClient userClient;

    public PostHandler() {
        super(MessageTargetNotifyEnum.POST);
    }

    @Override
    public List<UserDTO> getUsers(MessageTargetNotifyDTO messageTargetNotifyDTO) {
        if (Objects.isNull(messageTargetNotifyDTO.getTargetConfig())) {
            return Collections.emptyList();
        }
        List<MessagePostDTO> messagePostDTOS = JsonUtils.deserialize(messageTargetNotifyDTO.getTargetConfig(), new TypeReference<List<MessagePostDTO>>() {
        });
        if (CollectionUtils.isEmpty(messagePostDTOS)) {
            return Collections.emptyList();
        }
        List<UserDTO> userDTOS = new ArrayList<>();
        messagePostDTOS.stream().forEach(messagePostDTO -> {
            userDTOS.addAll(this.userClient.positionUsers(messagePostDTO.getPostId()));
        });
        return userDTOS;
    }

    @Override
    public Set<Long> getTargetIds(MessageTargetNotifyDTO messageTargetNotifyDTO) {
        if (Objects.isNull(messageTargetNotifyDTO.getTargetConfig())) {
            return Collections.emptySet();
        }
        List<MessagePostDTO> messagePostDTOS = JsonUtils.deserialize(messageTargetNotifyDTO.getTargetConfig(), new TypeReference<List<MessagePostDTO>>() {
        });
        return messagePostDTOS.stream().map(MessagePostDTO::getPostId).collect(Collectors.toSet());
    }
}
