package com.guhecloud.cube.biz.fusion.communicate.server.conference.application.commandgateway.command;

import com.guhecloud.cube.biz.fusion.communicate.server.conference.domain.enumeration.CallingTypeEnum;
import com.guhecloud.cube.biz.fusion.communicate.server.conference.domain.enumeration.MediaTypeEnum;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

/**
 * 邀请用户
 *
 * @author lanchun
 * @date 2025/2/17 14:52
 */
@Data
public class InviteConferenceRoomUserCommand {

    /**
     * 房间号
     */
    @NotBlank(message = "房间号不能为空")
    private String roomNumber;

    /**
     * 通话类型
     */
    @NotNull(message = "通话类型不能为空")
    private CallingTypeEnum callingType;

    /**
     * 媒体类型
     */
    @NotNull(message = "媒体类型不能为空")
    private MediaTypeEnum mediaType;

    /**
     * 会议发起人
     */
    @NotNull(message = "会议发起人不能为空")
    private Long proposeUserId;

    /**
     * 会议用户
     */
    private Set<Long> conferenceUsers;
}
