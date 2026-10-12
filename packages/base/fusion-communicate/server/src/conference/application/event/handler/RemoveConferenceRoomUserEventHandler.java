package com.guhecloud.cube.biz.fusion.communicate.server.conference.application.event.handler;

import com.guhecloud.cube.biz.fusion.communicate.server.conference.domain.entity.ConferenceUser;
import com.guhecloud.cube.biz.fusion.communicate.server.conference.domain.event.RemoveConferenceRoomUserEvent;
import com.guhecloud.cube.biz.fusion.communicate.server.conference.infrastructure.message.RoomEventMessageSender;
import com.guhecloud.cube.biz.fusion.communicate.server.conference.infrastructure.message.dto.RemoveConferenceRoomUserMessage;
import com.guhecloud.cube.biz.fusion.communicate.server.third.base.strategy.FusionCommunicateStrategy;
import com.guhecloud.cube.biz.fusion.communicate.server.third.base.vo.AccountAllocationUserVO;
import com.guhecloud.platform.base.util.CollectionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
public class RemoveConferenceRoomUserEventHandler {

    private final RoomEventMessageSender messageSender;

    private final FusionCommunicateStrategy fusionCommunicateStrategy;

    public RemoveConferenceRoomUserEventHandler(RoomEventMessageSender messageSender, FusionCommunicateStrategy fusionCommunicateStrategy) {
        this.messageSender = messageSender;
        this.fusionCommunicateStrategy = fusionCommunicateStrategy;
    }

    @EventListener
    public void removeConferenceRoomUserEvent(final RemoveConferenceRoomUserEvent event) {
        Set<ConferenceUser> roomUsers = event.getConferenceRoom().getConferenceUsers().stream()
                .filter(user -> Objects.isNull(user.getDepartureTime()))
                .collect(Collectors.toSet());
        if (CollectionUtils.isEmpty(roomUsers)) {
            return;
        }
        Set<Long> userIds = roomUsers.stream().map(ConferenceUser::getId).collect(Collectors.toSet());
        userIds.addAll(event.getRemoveConferenceRoomUsers().stream().map(ConferenceUser::getId).collect(Collectors.toSet()));
        List<AccountAllocationUserVO> onlineUsers = fusionCommunicateStrategy.getFusionCommunicateAccount().getOnlineUserIds(userIds);
        if (!CollectionUtils.isEmpty(onlineUsers)) {
            Set<Long> sendUserIds = onlineUsers.stream().map(AccountAllocationUserVO::getUserId).collect(Collectors.toSet());
            //踢出用户
            fusionCommunicateStrategy.getFusionCommunicateRoom().removeConferenceRoomUsers(event.getConferenceRoom().getRoom().getNumber(),
                                                                                           event.getRemoveConferenceRoomUsers().stream()
                                                                                                   .map(ConferenceUser::getId)
                                                                                                   .collect(Collectors.toSet()));
            Set<String> terminals = new HashSet<>();
            onlineUsers.forEach(onlineUser ->
                                        terminals.add(onlineUser.getAccount().getNumber().split("::")[0])
                               );
            RemoveConferenceRoomUserMessage message = RemoveConferenceRoomUserMessage.builder()
                    .roomNumber(event.getConferenceRoom().getRoom().getNumber())
                    .members(event.getRemoveConferenceRoomUsers().stream().map(ConferenceUser::getId).collect(Collectors.toSet()))
                    .build();
            messageSender.sendRemoveConferenceRoomUserMessage(message, sendUserIds, terminals);
        }
    }
}
