package com.guhecloud.cube.base.videogw.server.common.application.commandgateway.command;

import lombok.Data;

@Data
public class VideoBaseCommand {

    /**
     * 命令id
     */
    private Long id;

    /**
     * 设备主键
     */
    private String deviceId;

    /**
     * 请求id
     */
    private String requestId;
}
