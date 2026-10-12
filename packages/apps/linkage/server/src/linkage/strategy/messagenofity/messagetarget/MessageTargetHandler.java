package com.guhecloud.cube.biz.linkage.server.linkage.strategy.messagenofity.messagetarget;

import com.guhecloud.apps.linkage.metadata.dto.MessageTargetNotifyDTO;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;

import java.util.List;
import java.util.Set;

public interface MessageTargetHandler {

    boolean supports(MessageTargetNotifyDTO messageTargetNotifyDTO);

    List<UserDTO> getUsers(MessageTargetNotifyDTO messageTargetNotifyDTO);

    Set<Long> getTargetIds(MessageTargetNotifyDTO messageTargetNotifyDTO);
}
