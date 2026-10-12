package com.guhecloud.cube.customization.xialing.server.liangcang.vo;

import com.guhecloud.cube.customization.xialing.server.liangcang.enums.DoorGasType;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 门禁气体联动配置分页记录。
 *
 * @param configId 配置 ID
 * @param doorDeviceId 门禁设备中心本地 ID
 * @param doorName 门禁设备名称
 * @param deviceCode 门禁设备编码
 * @param areaName 所属区域名称
 * @param gasLinkageEnabled 是否开启气体联动
 * @param dataValidSeconds 气体数据有效时长，单位秒
 * @param configuredGasTypes 已配置气体类型
 * @param updateTime 最后更新时间
 */
public record DoorGasLinkageConfigPageResponse(
        Long configId,
        Long doorDeviceId,
        String doorName,
        String deviceCode,
        String areaName,
        Boolean gasLinkageEnabled,
        Integer dataValidSeconds,
        List<DoorGasType> configuredGasTypes,
        LocalDateTime updateTime
) {
}
