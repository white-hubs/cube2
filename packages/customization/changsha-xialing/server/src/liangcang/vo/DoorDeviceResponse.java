package com.guhecloud.cube.customization.xialing.server.liangcang.vo;

/**
 * 门禁设备中心基础信息。
 *
 * @param deviceName 设备名称
 * @param deviceCode 设备编码
 * @param areaId 区域 ID 路径
 * @param areaName 区域名称路径
 * @param productCategoryName 产品品类名称
 */
public record DoorDeviceResponse(
        String deviceName,
        String deviceCode,
        String areaId,
        String areaName,
        String productCategoryName
) {
}
