package com.guhecloud.cube.biz.fusion.communicate.server.conference.application.commandgateway;

import com.guhecloud.cube.biz.fusion.communicate.server.conference.application.commandgateway.command.CloseConferenceRoomCommand;
import com.guhecloud.cube.biz.fusion.communicate.server.conference.application.commandgateway.command.InviteConferenceRoomUserCommand;
import com.guhecloud.cube.biz.fusion.communicate.server.conference.application.commandgateway.command.JoinConferenceRoomCommand;
import com.guhecloud.cube.biz.fusion.communicate.server.conference.application.commandgateway.command.LeaveConferenceRoomCommand;
import com.guhecloud.cube.biz.fusion.communicate.server.conference.application.commandgateway.command.ProposeConferenceCommand;
import com.guhecloud.cube.biz.fusion.communicate.server.conference.application.commandgateway.command.RefuseJoinConferenceRoomCommand;
import com.guhecloud.cube.biz.fusion.communicate.server.conference.application.commandgateway.command.RemoveConferenceUserCommand;
import com.guhecloud.cube.biz.fusion.communicate.server.conference.application.commandgateway.validator.CloseConferenceRoomValidator;
import com.guhecloud.cube.biz.fusion.communicate.server.conference.application.commandgateway.validator.InviteUserValidator;
import com.guhecloud.cube.biz.fusion.communicate.server.conference.application.commandgateway.validator.JoinConferenceRoomValidator;
import com.guhecloud.cube.biz.fusion.communicate.server.conference.application.commandgateway.validator.LeaveConferenceRoomValidator;
import com.guhecloud.cube.biz.fusion.communicate.server.conference.application.commandgateway.validator.ProposeConferenceRoomValidator;
import com.guhecloud.cube.biz.fusion.communicate.server.conference.application.commandgateway.validator.RemoveConferenceUserValidator;
import com.guhecloud.cube.biz.fusion.communicate.server.conference.domain.aggregates.ConferenceRoom;
import com.guhecloud.cube.biz.fusion.communicate.server.conference.domain.entity.ConferenceInviteUser;
import com.guhecloud.cube.biz.fusion.communicate.server.conference.domain.entity.ConferenceUser;
import com.guhecloud.cube.biz.fusion.communicate.server.conference.domain.enumeration.DepartureTypeEnum;
import com.guhecloud.cube.biz.fusion.communicate.server.conference.domain.event.CloseConferenceRoomEvent;
import com.guhecloud.cube.biz.fusion.communicate.server.conference.domain.event.CreateConferenceRoomEvent;
import com.guhecloud.cube.biz.fusion.communicate.server.conference.domain.event.InviteConferenceRoomUserEvent;
import com.guhecloud.cube.biz.fusion.communicate.server.conference.domain.event.JoinConferenceRoomEvent;
import com.guhecloud.cube.biz.fusion.communicate.server.conference.domain.event.LeaveConferenceRoomEvent;
import com.guhecloud.cube.biz.fusion.communicate.server.conference.domain.event.ProposeConferenceRoomEvent;
import com.guhecloud.cube.biz.fusion.communicate.server.conference.domain.event.RefuseJoinConferenceRoomEvent;
import com.guhecloud.cube.biz.fusion.communicate.server.conference.domain.event.RemoveConferenceRoomUserEvent;
import com.guhecloud.cube.biz.fusion.communicate.server.conference.domain.factory.ConferenceRoomFactory;
import com.guhecloud.cube.biz.fusion.communicate.server.conference.domain.factory.ConferenceRoomInviteUserFactory;
import com.guhecloud.cube.biz.fusion.communicate.server.conference.domain.factory.ConferenceRoomUserFactory;
import com.guhecloud.cube.biz.fusion.communicate.server.conference.infrastructure.repository.ConferenceInviteRecordRepository;
import com.guhecloud.cube.biz.fusion.communicate.server.conference.infrastructure.repository.ConferenceRoomRepository;
import com.guhecloud.cube.biz.fusion.communicate.server.conference.infrastructure.repository.ConferenceRoomUserRepository;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ConferenceRoomCommandService {

    private final ConferenceRoomRepository conferenceRoomRepository;

    private final ConferenceRoomUserRepository conferenceRoomUserRepository;

    private final ConferenceInviteRecordRepository conferenceInviteRecordRepository;

    private final JoinConferenceRoomValidator joinConferenceRoomValidator;

    private final LeaveConferenceRoomValidator leaveConferenceRoomValidator;

    private final RemoveConferenceUserValidator removeConferenceUserValidator;

    private final CloseConferenceRoomValidator closeConferenceRoomValidator;

    private final InviteUserValidator inviteUserValidator;

    private final ProposeConferenceRoomValidator proposeConferenceRoomValidator;

    private final ApplicationEventPublisher applicationEventPublisher;

    /**
     * 创建房间
     *
     * @return 房间号
     */
    public String createConferenceRoom() {
        CreateConferenceRoomEvent createConferenceRoomEvent = new CreateConferenceRoomEvent(this);
        this.applicationEventPublisher.publishEvent(createConferenceRoomEvent);
        return createConferenceRoomEvent.get();
    }

    /**
     * 发起会议
     *
     * @param command 发起参数
     *
     * @return Long 会议房间id
     */
    public Long proposeConferenceRoom(ProposeConferenceCommand command) {
        proposeConferenceRoomValidator.validate(command);
        ConferenceRoom conferenceRoom = ConferenceRoomFactory.createConferenceRoom(command);
        conferenceRoomRepository.save(conferenceRoom);
        this.applicationEventPublisher.publishEvent(new ProposeConferenceRoomEvent(this, conferenceRoom));
        return conferenceRoom.getId();
    }

    /**
     * 邀请用户加入会议房间
     *
     * @param command 邀请用户加入命令
     *
     * @return
     */
    public void inviteUser(InviteConferenceRoomUserCommand command) {
        Optional<ConferenceRoom> conferenceRoom = conferenceRoomRepository.getConferenceRoom(command.getRoomNumber());
        inviteUserValidator.valid(conferenceRoom.orElse(null));
        ConferenceRoom room = conferenceRoom.get();
        room.getConference().setCallingType(command.getCallingType());
        room.getConference().setMediaType(command.getMediaType());
        conferenceRoomRepository.updateBaseInfo(room);
        Set<ConferenceInviteUser> conferenceInviteUser = ConferenceRoomInviteUserFactory.createConferenceInviteUser(command.getConferenceUsers());
        conferenceInviteRecordRepository.saveConferenceInviteRecord(room.getId(),
                                                                    room.getRoom().getNumber(),
                                                                    conferenceInviteUser);
        this.applicationEventPublisher.publishEvent(new InviteConferenceRoomUserEvent(this, room, command.getProposeUserId(), conferenceInviteUser));
    }

    /**
     * 用户拒绝加入房间
     *
     * @param command 拒绝参数
     */
    public void refuseJoin(RefuseJoinConferenceRoomCommand command) {
        Optional<ConferenceRoom> room = conferenceRoomRepository.getConferenceRoom(command.getRoomNumber());
        inviteUserValidator.valid(room.orElse(null));
        //发送拒绝事件
        applicationEventPublisher.publishEvent(new RefuseJoinConferenceRoomEvent(this, room.get(), command.getUserId()));
    }

    /**
     * 用户加入会议房间
     *
     * @param command 加入房间参数
     *
     * @return 加入后的唯一标识
     */
    public Long joinConferenceRoom(JoinConferenceRoomCommand command) {
        Optional<ConferenceRoom> conferenceRoom = conferenceRoomRepository.getConferenceRoom(command.getRoomNumber());
        joinConferenceRoomValidator.valid(conferenceRoom.orElse(null));
        ConferenceUser conferenceRoomUser = ConferenceRoomUserFactory
                .createConferenceRoomUser(SecurityContextHolder.getPrincipal().getId());
        conferenceRoomUser.setDepartureTime(null);
        conferenceRoomUser.setDepartureType(null);
        ConferenceRoom room = conferenceRoom.get();
        room.addConferenceUser(conferenceRoomUser);
        Long id = conferenceRoomUserRepository.saveConferenceUser(room.getId(), room.getRoom().getNumber(), conferenceRoomUser);
        //发送加入事件
        applicationEventPublisher.publishEvent(new JoinConferenceRoomEvent(this, room, conferenceRoomUser));
        return id;
    }

    /**
     * 离开会议房间
     *
     * @param command 离开房间参数
     *
     * @return
     */
    public Long leaveConferenceRoom(LeaveConferenceRoomCommand command) {
        Optional<ConferenceRoom> conferenceRoom = conferenceRoomRepository.getConferenceRoom(command.getRoomNumber());
        leaveConferenceRoomValidator.valid(conferenceRoom.orElse(null));
        ConferenceRoom room = conferenceRoom.get();
        ConferenceUser conferenceUser = room.getConferenceUserById(SecurityContextHolder.getPrincipal().getId());
        conferenceUser.setDepartureTime(DateUtils.now());
        conferenceUser.setDepartureType(DepartureTypeEnum.PROACTIVELY_WITHDRAW);
        Long id = conferenceRoomUserRepository.saveConferenceUser(room.getId(), room.getRoom().getNumber(), conferenceUser);
        //发送离开事件
        applicationEventPublisher.publishEvent(new LeaveConferenceRoomEvent(this, room, conferenceUser));
        return id;
    }

    /**
     * 踢出会议房间用户
     *
     * @param command 踢出参数
     */
    public void removeConferenceRoomUser(RemoveConferenceUserCommand command) {
        Optional<ConferenceRoom> conferenceRoom = conferenceRoomRepository.getConferenceRoom(command.getRoomNumber());
        removeConferenceUserValidator.valid(conferenceRoom.orElse(null));
        ConferenceRoom room = conferenceRoom.get();
        room.settingUserDepartureTypeAndTime(command.getUserIds(), DepartureTypeEnum.KICK_OUT, DateUtils.now());
        Set<ConferenceUser> usersByDepartureType = room.getUsersByDepartureType(command.getUserIds(), DepartureTypeEnum.KICK_OUT);
        conferenceRoomUserRepository.updateConferenceUser(room.getId(), room.getRoom().getNumber(), usersByDepartureType);
        //发送踢出事件
        applicationEventPublisher.publishEvent(new RemoveConferenceRoomUserEvent(this, room, usersByDepartureType));
    }

    /**
     * 关闭房间
     *
     * @param command 关闭房间参数
     */
    public void closeConferenceRoom(CloseConferenceRoomCommand command) {
        Optional<ConferenceRoom> conferenceRoom = conferenceRoomRepository.getConferenceRoom(command.getRoomNumber());
        closeConferenceRoomValidator.valid(conferenceRoom.orElse(null));
        ConferenceRoom room = conferenceRoom.get();
        room.settingUserDepartureType(DepartureTypeEnum.ROOM_CLOSE, DateUtils.now());
        Set<ConferenceUser> usersByDepartureType = room.getUsersByDepartureType(DepartureTypeEnum.ROOM_CLOSE);
        conferenceRoomUserRepository.updateConferenceUser(room.getId(), room.getRoom().getNumber(), usersByDepartureType);
        //发送房间关闭事件
        this.applicationEventPublisher.publishEvent(new CloseConferenceRoomEvent(this, room, usersByDepartureType));
    }
}
