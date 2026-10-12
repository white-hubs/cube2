package com.guhecloud.cube.biz.subsystem.broadcast.server.archive.application.commandgateway.command;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 循环播放参数
 *
 * @author lanchun
 * @date 2023/3/23 9:49
 */
@Data
public class BroadcastPlayTextCirculateCommand {

    /**
     * 模板主键
     */
    @NotNull(message = "模板主键不能为空")
    private Long id;

    /**
     * 广播模板变量参数
     */
    private List<BroadcastPlayVariableParam> broadcastPlayVariableParams;

    /**
     * 播放终端设备
     */
    @Valid
    @NotNull(message = "播放设备不能为空")
    @Size(min = 1, message = "播放设备数量不能小于1")
    private List<BroadcastPlayTextDeviceCommand> broadcastPlayDevices;

    /**
     * 开始时间
     */
    @NotNull(message = "开始时间不能为空")
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    @NotNull(message = "结束时间不能为空")
    private LocalDateTime endTime;
}
