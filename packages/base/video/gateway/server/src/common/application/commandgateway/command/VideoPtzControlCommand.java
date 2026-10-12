package com.guhecloud.cube.base.videogw.server.common.application.commandgateway.command;

import com.guhecloud.cube.base.videogw.server.common.domain.enumeration.VideoPtzActionEnum;
import com.guhecloud.cube.base.videogw.server.common.domain.enumeration.VideoPtzCommandEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * 摄像机云台控制请求参数
 *
 * @author lanchun
 * @date 2023/11/9 14:58
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class VideoPtzControlCommand extends VideoBaseCommand{

    /**
     * 控制动作
     */
    @NotNull(message = "控制动作不能为空")
    private VideoPtzActionEnum action;

    /**
     * 控制命令
     */
    @NotNull(message = "控制命令不能为空")
    private VideoPtzCommandEnum command;

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
