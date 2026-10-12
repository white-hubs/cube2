package com.guhecloud.cube.biz.fusion.communicate.server.conference.application.event.handler;

import com.guhecloud.cube.biz.fusion.communicate.server.conference.domain.entity.ConferenceInviteUser;
import com.guhecloud.cube.biz.fusion.communicate.server.conference.domain.entity.ConferenceUser;
import com.guhecloud.cube.biz.fusion.communicate.server.conference.domain.event.CloseConferenceRoomEvent;
import com.guhecloud.cube.biz.fusion.communicate.server.conference.infrastructure.message.RoomEventMessageSender;
import com.guhecloud.cube.biz.fusion.communicate.server.conference.infrastructure.message.dto.CloseConferenceRoomMessage;
import com.guhecloud.cube.biz.fusion.communicate.server.third.base.strategy.FusionCommunicateStrategy;
import com.guhecloud.cube.biz.fusion.communicate.server.third.base.vo.AccountAllocationUserVO;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
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
public class CloseConferenceRoomEventHandler {

    private final RoomEventMessageSender messageSender;

    private final FusionCommunicateStrategy fusionCommunicateStrategy;

    public CloseConferenceRoomEventHandler(RoomEventMessageSender messageSender, FusionCommunicateStrategy fusionCommunicateStrategy) {
        this.messageSender = messageSender;
        this.fusionCommunicateStrategy = fusionCommunicateStrategy;
    }

    @EventListener
    public void closeConferenceRoomEvent(final CloseConferenceRoomEvent event) {
        Set<ConferenceUser> roomUsers = event.getConferenceRoom().getConferenceUsers().stream()
                .filter(user -> Objects.isNull(user.getDepartureTime()))
                .collect(Collectors.toSet());
        Set<Long> userIds = roomUsers.stream().map(ConferenceUser::getId).collect(Collectors.toSet());
        userIds.addAll(event.getConferenceRoom().getConferenceInviteUsers().stream().map(ConferenceInviteUser::getUserId)
                               .collect(Collectors.toSet()));
        List<AccountAllocationUserVO> onlineUsers = fusionCommunicateStrategy.getFusionCommunicateAccount().getOnlineUserIds(userIds);
        //关闭房间
        fusionCommunicateStrategy.getFusionCommunicateRoom().disbandRoomRequest(event.getConferenceRoom().getRoom().getNumber());
        if (!CollectionUtils.isEmpty(onlineUsers)) {
            Set<String> terminals = new HashSet<>();
            onlineUsers.forEach(onlineUser ->
                                        terminals.add(onlineUser.getAccount().getNumber().split("::")[0])
                               );
            Set<Long> sendUserIds = onlineUsers.stream().map(AccountAllocationUserVO::getUserId).collect(Collectors.toSet());
            sendUserIds.remove(SecurityContextHolder.getPrincipal().getId());
            CloseConferenceRoomMessage message = CloseConferenceRoomMessage.builder()
                    .roomNumber(event.getConferenceRoom().getRoom().getNumber())
                    .build();
            messageSender.sendCloseConferenceRoomMsg(message, sendUserIds, terminals);
        }
    }
}
