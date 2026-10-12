package com.guhecloud.cube.biz.fusion.communicate.server.conference.application.event.handler;

import com.guhecloud.cube.biz.fusion.communicate.server.conference.domain.entity.ConferenceUser;
import com.guhecloud.cube.biz.fusion.communicate.server.conference.domain.event.JoinConferenceRoomEvent;
import com.guhecloud.cube.biz.fusion.communicate.server.conference.infrastructure.message.RoomEventMessageSender;
import com.guhecloud.cube.biz.fusion.communicate.server.conference.infrastructure.message.dto.JoinConferenceRoomMessage;
import com.guhecloud.cube.biz.fusion.communicate.server.conference.infrastructure.message.dto.UserOtherTerminalJoinRoomMessage;
import com.guhecloud.cube.biz.fusion.communicate.server.third.base.strategy.FusionCommunicateStrategy;
import com.guhecloud.cube.biz.fusion.communicate.server.third.base.vo.AccountAllocationUserVO;
import com.guhecloud.platform.base.context.SessionContextHolder;
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
public class JoinConferenceRoomEventHandler {

    private final RoomEventMessageSender roomEventMessageSender;

    private final FusionCommunicateStrategy fusionCommunicateStrategy;

    public JoinConferenceRoomEventHandler(RoomEventMessageSender roomEventMessageSender, FusionCommunicateStrategy fusionCommunicateStrategy
                                         ) {
        this.roomEventMessageSender = roomEventMessageSender;
        this.fusionCommunicateStrategy = fusionCommunicateStrategy;
    }

    /**
     * 加入会议事件处理
     *
     * @param event
     */
    @EventListener
    public void joinConferenceRoomEvent(final JoinConferenceRoomEvent event) {
        //发送加入消息事件
        Set<ConferenceUser> conferenceUsers = event.getConferenceRoom().getConferenceUsers();
        Set<ConferenceUser> roomUsers = conferenceUsers.stream().filter(user -> Objects.isNull(user.getDepartureTime()))
                .collect(Collectors.toSet());
        Set<Long> userIds = roomUsers.stream().map(ConferenceUser::getId).collect(Collectors.toSet());
        List<AccountAllocationUserVO> onlineUsers = fusionCommunicateStrategy.getFusionCommunicateAccount().getOnlineUserIds(userIds);
        if (!CollectionUtils.isEmpty(onlineUsers)) {
            Set<Long> sendUserIds = onlineUsers.stream().map(AccountAllocationUserVO::getUserId).collect(Collectors.toSet());
            fusionCommunicateStrategy.getFusionCommunicateRoom().joinConferenceRoom(event.getConferenceRoom().getRoom().getNumber(),
                                                                                    Collections.singleton(event.getJoinConferenceRoomUser()
                                                                                                                  .getId()));
            Set<String> terminals = new HashSet<>();
            onlineUsers.forEach(onlineUser ->
                                        terminals.add(onlineUser.getAccount().getNumber().split("::")[0])
                               );
            JoinConferenceRoomMessage message = JoinConferenceRoomMessage.builder()
                    .callingUser(event.getConferenceRoom().getProposeUserId())
                    .callingType(event.getConferenceRoom().getConference().getCallingType().name())
                    .mediaType(event.getConferenceRoom().getConference().getMediaType().name())
                    .roomNumber(event.getConferenceRoom().getRoom().getNumber())
                    .member(event.getJoinConferenceRoomUser().getId())
                    .build();
            roomEventMessageSender.sendJoinConferenceRoomMsg(message, sendUserIds, terminals);
            String account = SessionContextHolder.getRequester().getTerminal() + "::" + event.getJoinConferenceRoomUser().getId();
            //发送用户已在其它端加入消息
            Set<AccountAllocationUserVO> otherAccountAllocationUsers = onlineUsers.stream()
                    .filter(user -> event.getJoinConferenceRoomUser().getId().equals(user.getUserId())
                                    && !account.equals(user.getAccount().getNumber()))
                    .collect(Collectors.toSet());
            if (!CollectionUtils.isEmpty(otherAccountAllocationUsers)) {
                Set<String> otherTerminal = otherAccountAllocationUsers.stream().map(user -> user.getAccount().getNumber().split("::")[0])
                        .collect(Collectors.toSet());
                UserOtherTerminalJoinRoomMessage userOtherTerminalJoinRoomMessage = new UserOtherTerminalJoinRoomMessage(event
                                                                                                                                 .getConferenceRoom()
                                                                                                                                 .getRoom()
                                                                                                                                 .getNumber());
                roomEventMessageSender.sendUserOtherTerminalJoinMsg(userOtherTerminalJoinRoomMessage,
                                                                    Collections.singleton(event.getJoinConferenceRoomUser().getId()),
                                                                    otherTerminal);
            }
        }
    }
}
