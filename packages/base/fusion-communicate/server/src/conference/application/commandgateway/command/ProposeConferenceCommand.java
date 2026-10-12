package com.guhecloud.cube.biz.fusion.communicate.server.conference.application.commandgateway.command;

import com.guhecloud.cube.biz.fusion.communicate.server.conference.domain.enumeration.CallingTypeEnum;
import com.guhecloud.cube.biz.fusion.communicate.server.conference.domain.enumeration.MediaTypeEnum;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

/**
 * 发起会议
 *
 * @author lanchun
 * @date 2025/2/12 9:49
 */
@Data
public class ProposeConferenceCommand {

    /**
     * 编号
     */
    private String code;

    /**
     * 名称
     */
    private String name;

    /**
     * 房间号
     */
    @NotBlank(message = "房间号不能为空")
    private String roomNumber;

    /**
     * 会议室类型
     */
    private String conferenceType;

    /**
     * 媒体类型
     */
    @NotNull(message = "媒体类型不能为空")
    private MediaTypeEnum mediaType;

    /**
     * 通话类型
     */
    @NotNull(message = "通话类型不能为空")
    private CallingTypeEnum callingType;

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
