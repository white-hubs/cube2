package com.guhecloud.cube.customization.xialing.server.liangcang.vo;

import com.guhecloud.cube.customization.xialing.server.liangcang.enums.DoorGasType;

import java.math.BigDecimal;

/**
 * 单种气体联动配置保存参数。
 *
 * @param gasType 气体类型
 * @param sensorDeviceId 气体传感器设备中心本地 ID
 * @param lowerThreshold 氧气下限，其他气体不使用
 * @param upperThreshold 气体上限
 * @param unit 阈值单位
 */
public record DoorGasConfigRequest(
        DoorGasType gasType,
        Long sensorDeviceId,
        BigDecimal lowerThreshold,
        BigDecimal upperThreshold,
        String unit
) {
}
