package com.guhecloud.cube.biz.subsystem.broadcast.server.archive.application.commandgateway.command;

import lombok.Data;

import jakarta.validation.constraints.NotNull;

/**
 * 广播停止播放命令参数
 *
 * @author lanchun
 * @date 2023/3/30 11:20
 */
@Data
public class BroadcastPlayTextStopCommand {

    /**
     * 设备主键
     */
    @NotNull(message = "设备主键不能为空")
    private Long deviceId;
}
