package com.guhecloud.cube.customization.xialing.server.liangcang.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.guhecloud.cube.customization.xialing.server.liangcang.enums.DoorGasType;

import java.math.BigDecimal;

/**
 * 单种气体联动配置详情。空字段不返回，确保 CO2、PH3 仅返回上限。
 *
 * @param gasType 气体类型
 * @param sensorDeviceId 气体传感器设备 ID
 * @param sensorDeviceName 气体传感器设备名称
 * @param lowerThreshold 氧气下限，仅 O2 返回
 * @param upperThreshold 气体上限
 * @param unit 阈值单位
 * @param configured 是否已配置传感器
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record DoorGasConfigResponse(
        DoorGasType gasType,
        Long sensorDeviceId,
        String sensorDeviceName,
        BigDecimal lowerThreshold,
        BigDecimal upperThreshold,
        String unit,
        Boolean configured
) {
}
