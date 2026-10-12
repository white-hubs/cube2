package com.guhecloud.cube.srv.gen.adapter.video.server.dto.request;

import lombok.Data;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Data
public class VideoPtzControlRequest {

    /**
     * 控制动作
     */
    @NotNull(message = "控制动作不能为空")
    private String action;

    /**
     * 控制命令
     */
    @NotNull(message = "控制命令不能为空")
    private String command;

    /**
     * 云台速度
     */
    @Min(value = 0, message = "云台速度不能小于1")
    private Integer speed;

    /**
     * 预设Id
     */
    private Integer presetId;
}
