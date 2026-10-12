package com.guhecloud.cube.biz.fusion.communicate.server.conference.application.event.handler;

import com.guhecloud.cube.biz.fusion.communicate.server.conference.domain.entity.ConferenceInviteUser;
import com.guhecloud.cube.biz.fusion.communicate.server.conference.domain.entity.ConferenceUser;
import com.guhecloud.cube.biz.fusion.communicate.server.conference.domain.event.InviteConferenceRoomUserEvent;
import com.guhecloud.cube.biz.fusion.communicate.server.conference.infrastructure.message.RoomEventMessageSender;
import com.guhecloud.cube.biz.fusion.communicate.server.conference.infrastructure.message.dto.ProposeConferenceRoomMessage;
import com.guhecloud.cube.biz.fusion.communicate.server.third.base.strategy.FusionCommunicateStrategy;
import com.guhecloud.cube.biz.fusion.communicate.server.third.base.vo.AccountAllocationUserVO;
import com.guhecloud.platform.base.util.CollectionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
public class inviteUserEventHandler {

    private final RoomEventMessageSender roomEventMessageSender;

    private final FusionCommunicateStrategy fusionCommunicateStrategy;

    public inviteUserEventHandler(RoomEventMessageSender roomEventMessageSender, FusionCommunicateStrategy fusionCommunicateStrategy) {
        this.roomEventMessageSender = roomEventMessageSender;
        this.fusionCommunicateStrategy = fusionCommunicateStrategy;
    }

    @EventListener
    public void inviteUserEvent(final InviteConferenceRoomUserEvent event) {
        List<AccountAllocationUserVO> onlineUsers = fusionCommunicateStrategy.getFusionCommunicateAccount()
                .getOnlineUserIds(event.getConferenceRoom().getConferenceUsers().stream().map(ConferenceUser::getId)
                                          .collect(Collectors.toSet()));
        Set<Long> inviteUser = event.getInviteUsers().stream()
                .map(ConferenceInviteUser::getUserId).collect(Collectors.toSet());
        List<AccountAllocationUserVO> onlineInviteUsers = fusionCommunicateStrategy.getFusionCommunicateAccount()
                .getOnlineUserIds(inviteUser);
        onlineUsers.addAll(onlineInviteUsers);
        if (!CollectionUtils.isEmpty(onlineUsers)) {
            Set<Long> userIds = onlineUsers.stream().map(AccountAllocationUserVO::getUserId).collect(Collectors.toSet());
            Set<String> terminals = new HashSet<>();
            onlineUsers.forEach(onlineUser ->
                                        terminals.add(onlineUser.getAccount().getNumber().split("::")[0])
                               );
            ProposeConferenceRoomMessage message = ProposeConferenceRoomMessage.builder()
                    .callingUser(event.getInviteUserId())
                    .roomNumber(event.getConferenceRoom().getRoom().getNumber())
                    .callingUser(event.getConferenceRoom().getProposeUserId())
                    .callingType(event.getConferenceRoom().getConference().getCallingType().name())
                    .mediaType(event.getConferenceRoom().getConference().getMediaType().name())
                    .members(userIds)
                    .build();
            roomEventMessageSender.sendProposeConferenceRoomMsg(message, event.getInviteUsers().stream()
                    .map(ConferenceInviteUser::getUserId).collect(Collectors.toSet()), terminals);
        }
    }
}
