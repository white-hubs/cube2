package com.guhecloud.cube.base.videogw.server.facade.video.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VideoPtzControlRequest {

    /**
     * 控制动作
     */
    private String action;

    /**
     * 控制命令
     */
    private String command;

    /**
     * 云台速度
     */
    private Integer speed;

    /**
     * 预设Id
     */
    private Integer presetId;
}
