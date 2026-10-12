package com.guhecloud.cube.biz.linkage.server.linkage.strategy.messagenofity.messagetarget.handler;

import com.fasterxml.jackson.core.type.TypeReference;
import com.guhecloud.apps.linkage.metadata.dto.MessageTargetNotifyDTO;
import com.guhecloud.apps.linkage.metadata.enumeration.MessageTargetNotifyEnum;
import com.guhecloud.cube.biz.linkage.server.linkage.strategy.messagenofity.messagetarget.MessageTargetAbstractHandler;
import com.guhecloud.cube.biz.linkage.server.linkage.strategy.messagenofity.messagetarget.dto.MessageRoleDTO;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.biz.ouaa.client.queryrier.UserQueryService;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
public class RoleHandler extends MessageTargetAbstractHandler {

    @Autowired
    private UserQueryService userQueryService;

    public RoleHandler() {
        super(MessageTargetNotifyEnum.ROLE);
    }

    @Override
    public List<UserDTO> getUsers(MessageTargetNotifyDTO messageTargetNotifyDTO) {
        if (Objects.isNull(messageTargetNotifyDTO.getTargetConfig())) {
            return Collections.emptyList();
        }
        List<MessageRoleDTO> messageRoleDTOS = JsonUtils.deserialize(messageTargetNotifyDTO.getTargetConfig(), new TypeReference<List<MessageRoleDTO>>() {
        });
        if (CollectionUtils.isEmpty(messageRoleDTOS)) {
            return Collections.emptyList();
        }
        List<UserDTO> userDTOS = userQueryService
                .roleIds(messageRoleDTOS.stream().map(MessageRoleDTO::getRoleId).collect(Collectors.toSet())).enabled().list();
        return userDTOS;
    }

    @Override
    public Set<Long> getTargetIds(MessageTargetNotifyDTO messageTargetNotifyDTO) {
        if (Objects.isNull(messageTargetNotifyDTO.getTargetConfig())) {
            return Collections.emptySet();
        }
        List<MessageRoleDTO> messageRoleDTOS = JsonUtils.deserialize(messageTargetNotifyDTO.getTargetConfig(), new TypeReference<List<MessageRoleDTO>>() {
        });
        return messageRoleDTOS.stream().map(MessageRoleDTO::getRoleId).collect(Collectors.toSet());
    }
}
