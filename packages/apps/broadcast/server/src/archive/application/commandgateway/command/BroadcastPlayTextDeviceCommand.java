package com.guhecloud.cube.biz.subsystem.broadcast.server.archive.application.commandgateway.command;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * 广播文本播放设备列表信息
 *
 * @author lanchun
 * @date 2022/12/12 16:16
 */
@Data
public class BroadcastPlayTextDeviceCommand {

    /**
     * 设备主键
     */
    @NotNull(message = "设备主键不能为空")
    private Long id;

    /**
     * 设备编号
     */
    @NotBlank(message = "设备编号不能为空")
    @Size(max = 100, message = "设备编号不能大于100个字符")
    private String code;

    /**
     * 设备名称
     */
    @NotBlank(message = "设备名称不能为空")
    @Size(max = 100, message = "设备名称不能大于100个字符")
    private String name;

    /**
     * 设备第三方标识
     */
    @NotBlank(message = "设备第三方标识不能为空")
    private String thirdPartyCode;
}
