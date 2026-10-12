package com.guhecloud.cube.biz.fusion.communicate.server.conference.application.event.handler;

import com.guhecloud.cube.biz.fusion.communicate.server.conference.domain.entity.ConferenceUser;
import com.guhecloud.cube.biz.fusion.communicate.server.conference.domain.event.LeaveConferenceRoomEvent;
import com.guhecloud.cube.biz.fusion.communicate.server.conference.infrastructure.message.RoomEventMessageSender;
import com.guhecloud.cube.biz.fusion.communicate.server.conference.infrastructure.message.dto.LeaveConferenceRoomMessage;
import com.guhecloud.cube.biz.fusion.communicate.server.third.base.strategy.FusionCommunicateStrategy;
import com.guhecloud.cube.biz.fusion.communicate.server.third.base.vo.AccountAllocationUserVO;
import com.guhecloud.platform.base.util.CollectionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
public class LeaveConferenceRoomEventHandler {

    private final RoomEventMessageSender messageSender;

    private final FusionCommunicateStrategy fusionCommunicateStrategy;

    public LeaveConferenceRoomEventHandler(RoomEventMessageSender messageSender, FusionCommunicateStrategy fusionCommunicateStrategy) {
        this.messageSender = messageSender;
        this.fusionCommunicateStrategy = fusionCommunicateStrategy;
    }

    @EventListener
    public void leaveConferenceRoomEvent(final LeaveConferenceRoomEvent event) {
        Set<ConferenceUser> roomUsers = event.getConferenceRoom().getConferenceUsers().stream()
                .filter(user -> Objects.isNull(user.getDepartureTime()))
                .collect(Collectors.toSet());
        if (CollectionUtils.isEmpty(roomUsers)) {
            return;
        }
        Set<Long> userIds = roomUsers.stream().map(ConferenceUser::getId).collect(Collectors.toSet());
        List<AccountAllocationUserVO> onlineUsers = fusionCommunicateStrategy.getFusionCommunicateAccount().getOnlineUserIds(userIds);
        if (!CollectionUtils.isEmpty(onlineUsers)) {
            fusionCommunicateStrategy.getFusionCommunicateRoom().leaveConferenceRoom(event.getConferenceRoom().getRoom().getNumber(),
                                                                                     Collections.singleton(event
                                                                                                                   .getLeaveConferenceRoomUser()
                                                                                                                   .getId()));
            userIds.remove(event.getLeaveConferenceRoomUser().getId());
            Set<String> terminals = new HashSet<>();
            onlineUsers.forEach(onlineUser ->
                                        terminals.add(onlineUser.getAccount().getNumber().split("::")[0])
                               );
            Set<Long> sendUserIds = onlineUsers.stream().map(AccountAllocationUserVO::getUserId).collect(Collectors.toSet());
            LeaveConferenceRoomMessage message = LeaveConferenceRoomMessage.builder()
                    .roomNumber(event.getConferenceRoom().getRoom().getNumber())
                    .member(event.getLeaveConferenceRoomUser().getId())
                    .build();
            messageSender.sendLeaveConferenceRoomMessage(message, sendUserIds, terminals);
        }
    }
}
