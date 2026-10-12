package com.guhecloud.cube.biz.fusion.communicate.server.conference.application.commandgateway.command;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;

/**
 * 加入房间
 *
 * @author lanchun
 * @date 2025/2/12 15:48
 */
@Data
public class JoinConferenceRoomCommand {

    /**
     * 房间号
     */
    @NotBlank(message = "房间号不能为空")
    private String roomNumber;
}
