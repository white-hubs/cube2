package com.guhecloud.cube.biz.fusion.communicate.server.conference.interfaces;

import com.guhecloud.cube.biz.fusion.communicate.server.conference.application.commandgateway.ConferenceRoomCommandService;
import com.guhecloud.cube.biz.fusion.communicate.server.conference.application.commandgateway.command.CloseConferenceRoomCommand;
import com.guhecloud.cube.biz.fusion.communicate.server.conference.application.commandgateway.command.InviteConferenceRoomUserCommand;
import com.guhecloud.cube.biz.fusion.communicate.server.conference.application.commandgateway.command.JoinConferenceRoomCommand;
import com.guhecloud.cube.biz.fusion.communicate.server.conference.application.commandgateway.command.LeaveConferenceRoomCommand;
import com.guhecloud.cube.biz.fusion.communicate.server.conference.application.commandgateway.command.ProposeConferenceCommand;
import com.guhecloud.cube.biz.fusion.communicate.server.conference.application.commandgateway.command.RefuseJoinConferenceRoomCommand;
import com.guhecloud.cube.biz.fusion.communicate.server.conference.application.commandgateway.command.RemoveConferenceUserCommand;
import com.guhecloud.cube.biz.fusion.communicate.server.conference.application.querygateway.ConferenceRoomQueryService;
import com.guhecloud.cube.biz.fusion.communicate.server.conference.interfaces.vo.ConferenceRoomVO;
import com.guhecloud.cube.biz.fusion.communicate.server.conference.interfaces.vo.RoomVO;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ConferenceRoomController {

    private final ConferenceRoomCommandService conferenceRoomCommandService;

    private final ConferenceRoomQueryService conferenceRoomQueryService;

    /**
     * 创建会议房间
     *
     * @return 房间号
     */
    @PostMapping("/conference-room/create")
    public RoomVO createConferenceRoom() {
        String room = conferenceRoomCommandService.createConferenceRoom();
        return new RoomVO(room);
    }

    /**
     * 会议房间发起
     *
     * @param command 发起参数
     *
     * @return 会议id
     */
    @PostMapping("/conference-room/propose")
    public Long proposeConferenceRoom(@Valid @RequestBody ProposeConferenceCommand command) {
        return this.conferenceRoomCommandService.proposeConferenceRoom(command);
    }

    /**
     * 会议房间邀请
     *
     * @param command 邀请参数
     */
    @PostMapping("/conference-room/invite")
    public void inviteUser(@Valid @RequestBody InviteConferenceRoomUserCommand command) {
        this.conferenceRoomCommandService.inviteUser(command);
    }

    /**
     * 拒绝加入会议
     *
     * @param command 拒绝参数
     */
    @PostMapping("/conference-room/refuse-join")
    public void refuseJoin(@Valid @RequestBody RefuseJoinConferenceRoomCommand command) {
        command.setUserId(SecurityContextHolder.getPrincipal().getId());
        this.conferenceRoomCommandService.refuseJoin(command);
    }

    /**
     * 加入会议房间
     *
     * @param command 加入参数
     *
     * @return 加入标识
     */
    @PostMapping("/conference-room/join")
    public Long joinConferenceRoom(@Valid @RequestBody JoinConferenceRoomCommand command) {
        return this.conferenceRoomCommandService.joinConferenceRoom(command);
    }

    /**
     * 离开会议房间
     *
     * @param command 离开参数
     *
     * @return 加入时的唯一标识
     */
    @PostMapping("/conference-room/leave")
    public Long leaveConferenceRoom(@Valid @RequestBody LeaveConferenceRoomCommand command) {
        return this.conferenceRoomCommandService.leaveConferenceRoom(command);
    }

    /**
     * 踢出会议房间用户
     *
     * @param command 踢出参数
     */
    @PostMapping("/conference-room/remove/user")
    public void removeConferenceRoomUser(@Valid @RequestBody RemoveConferenceUserCommand command) {
        this.conferenceRoomCommandService.removeConferenceRoomUser(command);
    }

    /**
     * 关闭房间
     *
     * @param command 关闭房间参数
     */
    @PostMapping("/conference-room/close")
    public void closeConferenceRoom(@Valid @RequestBody CloseConferenceRoomCommand command) {
        this.conferenceRoomCommandService.closeConferenceRoom(command);
    }

    /**
     * 获取会议房间信息
     *
     * @param roomNumber 房间号
     *
     * @return 会议房间信息
     */
    @GetMapping("/conference-room/{roomNumber}")
    public ConferenceRoomVO getConferenceRoom(@PathVariable("roomNumber") String roomNumber) {
        return this.conferenceRoomQueryService.getConferenceRoom(roomNumber);
    }
}
