package com.guhecloud.cube.srv.gen.adapter.access.control.server.model.jieshun.api.dto.request;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * author: ht
 * create: 2025-05-22 18:28
 */
@Data
public class DoorStatusUploadRequest {

    /**
     * 设备全局唯一标识
     * 示例：DVC-7F3A-48BD-91C2
     * 格式要求：32位UUID格式
     */
    @NotNull(message = "设备全局唯一标识不能为空")
    private String deviceGuid;
    /**
     * 设备显示名称
     * 示例："东门读卡器"、"地下车库闸机"
     * 长度限制：50个字符
     */
    private String deviceName;
    /**
     * 出入口类型
     */
    private Integer deviceIoType;
    /**
     * 设备运行状态
     */
    @NotBlank(message = "设备运行状态不能为空")
    private String deviceStatus;

}
