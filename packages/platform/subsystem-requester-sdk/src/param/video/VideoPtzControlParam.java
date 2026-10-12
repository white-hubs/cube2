package com.guhecloud.subsystem.requester.sdk.param.video;

import com.guhecloud.subsystem.requester.sdk.enumeration.video.VideoSubsytemPtzActionEnum;
import com.guhecloud.subsystem.requester.sdk.enumeration.video.VideoSubsytemPtzCommandEnum;
import lombok.Data;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * 摄像机云台控制请求参数
 *
 * @author lanchun
 * @date 2023/11/9 14:58
 */
@Data
public class VideoPtzControlParam {

    /**
     * 控制动作
     */
    @NotNull(message = "控制动作不能为空")
    private VideoSubsytemPtzActionEnum action;

    /**
     * 控制命令
     */
    @NotNull(message = "控制命令不能为空")
    private VideoSubsytemPtzCommandEnum command;

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
