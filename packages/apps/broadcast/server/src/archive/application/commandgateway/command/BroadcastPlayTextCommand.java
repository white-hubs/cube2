package com.guhecloud.cube.biz.subsystem.broadcast.server.archive.application.commandgateway.command;

import lombok.Data;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

/**
 * 广播文本播放命令参数
 *
 * @author lanchun
 * @date 2022/12/12 16:12
 */
@Data
public class BroadcastPlayTextCommand {

    /**
     * 模板主键
     */
    @NotNull(message = "模板主键不能为空")
    private Long id;

    /**
     * 广播计划主键
     */
    private Long broadcastPlayPlanId;

    /**
     * 播放次数
     */
    @NotNull(message = "播放次数不能为空")
    @Max(value = 99, message = "播放次数不能大于99次")
    private Integer broadcastPlayNum;

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
}
