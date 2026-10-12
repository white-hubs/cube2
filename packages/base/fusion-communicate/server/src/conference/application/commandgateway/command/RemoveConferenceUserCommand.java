package com.guhecloud.cube.biz.fusion.communicate.server.conference.application.commandgateway.command;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Set;

/**
 * 移除会议房间用户
 *
 * @author lanchun
 * @date 2025/2/12 15:45
 */
@Data
public class RemoveConferenceUserCommand {

    /**
     * 房间号
     */
    @NotBlank(message = "房间号不能为空")
    private String roomNumber;

    /**
     * 移除的用户id集合
     */
    @Size(min = 1, message = "踢除的用户不能为空")
    private Set<Long> userIds;
}
