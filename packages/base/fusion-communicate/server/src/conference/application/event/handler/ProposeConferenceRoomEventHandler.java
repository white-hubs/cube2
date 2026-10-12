package com.guhecloud.cube.biz.fusion.communicate.server.conference.application.event.handler;

import com.guhecloud.cube.biz.fusion.communicate.server.conference.domain.entity.ConferenceInviteUser;
import com.guhecloud.cube.biz.fusion.communicate.server.conference.domain.event.ProposeConferenceRoomEvent;
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
public class ProposeConferenceRoomEventHandler {

    private final RoomEventMessageSender messageSender;

    private final FusionCommunicateStrategy fusionCommunicateStrategy;

    public ProposeConferenceRoomEventHandler(RoomEventMessageSender messageSender, FusionCommunicateStrategy fusionCommunicateStrategy) {
        this.messageSender = messageSender;
        this.fusionCommunicateStrategy = fusionCommunicateStrategy;
    }

    @EventListener
    public void proposeConferenceRoomEvent(final ProposeConferenceRoomEvent event) {
        Set<ConferenceInviteUser> inviteUsers = event.getConferenceRoom().getConferenceInviteUsers();
        Set<Long> userIds = inviteUsers.stream().map(ConferenceInviteUser::getUserId).collect(Collectors.toSet());
        List<AccountAllocationUserVO> onlineUsers = fusionCommunicateStrategy.getFusionCommunicateAccount().getOnlineUserIds(userIds);
        if (!CollectionUtils.isEmpty(onlineUsers)) {
            Set<String> terminals = new HashSet<>();
            onlineUsers.forEach(onlineUser ->
                                        terminals.add(onlineUser.getAccount().getNumber().split("::")[0])
                               );
            Set<Long> sendUserIds = onlineUsers.stream().map(AccountAllocationUserVO::getUserId).collect(Collectors.toSet());
            sendUserIds.remove(event.getConferenceRoom().getProposeUserId());
            ProposeConferenceRoomMessage message = ProposeConferenceRoomMessage.builder()
                    .callingUser(event.getConferenceRoom().getProposeUserId())
                    .callingType(event.getConferenceRoom().getConference().getCallingType().name())
                    .mediaType(event.getConferenceRoom().getConference().getMediaType().name())
                    .roomNumber(event.getConferenceRoom().getRoom().getNumber())
                    .members(sendUserIds)
                    .build();
            messageSender.sendProposeConferenceRoomMsg(message, sendUserIds, terminals);
        }
    }
}
