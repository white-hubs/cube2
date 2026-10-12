package com.guhecloud.cube.biz.fusion.communicate.server.conference.application.commandgateway.command;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;

/**
 * 退出房间
 *
 * @author lanchun
 * @date 2025/2/12 15:49
 */
@Data
public class LeaveConferenceRoomCommand {

    /**
     * 房间号
     */
    @NotBlank(message = "房间号不能为空")
    private String roomNumber;
}
