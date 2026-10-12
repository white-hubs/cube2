package com.guhecloud.cube.biz.linkage.server.linkage.strategy.video.param;

import lombok.Data;

import jakarta.validation.constraints.NotNull;

@Data
public class VideoCtrlCommandParam {

    /**
     * 摄像机动作主键
     */
    @NotNull(message = "摄像机动作主键不能为空")
    private Long eventVideoId;
}
