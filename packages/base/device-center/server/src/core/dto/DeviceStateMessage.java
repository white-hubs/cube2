package com.guhecloud.cube.base.device.center.core.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 设备状态消息DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeviceStateMessage {

    /**
     * 设备ID
     */
    private Long deviceId;

    /**
     * 通信状态（1-正常，0-异常）
     */
    private Integer communicateState;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}