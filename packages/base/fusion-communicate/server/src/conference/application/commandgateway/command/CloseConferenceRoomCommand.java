package com.guhecloud.cube.biz.fusion.communicate.server.conference.application.commandgateway.command;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;

/**
 * 关闭房间参数
 *
 * @author lanchun
 * @date 2025/2/13 14:26
 */
@Data
public class CloseConferenceRoomCommand {

    /**
     * 房间号
     */
    @NotBlank(message = "房间号不能为空")
    private String roomNumber;
}
