package com.guhecloud.cube.biz.fusion.communicate.server.conference.application.commandgateway.command;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;

@Data
public class RefuseJoinConferenceRoomCommand {

    /**
     * 房间号
     */
    @NotBlank(message = "房间号不能为空")
    private String roomNumber;

    /**
     * 拒绝的用户id
     */
    private Long userId;
}
