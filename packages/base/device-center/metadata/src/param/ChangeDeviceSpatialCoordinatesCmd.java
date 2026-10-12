package com.guhecloud.cube.base.device.center.metadata.param;

import lombok.Data;

/**
 * 设备空间坐标绑定参数
 *
 * @author lanchun
 * @date 2025/6/17 18:14
 */
@Data
public class ChangeDeviceSpatialCoordinatesCmd {

    /**
     * 设备编号
     */
    private String code;

    /**
     * 空间坐标信息
     */
    private String spatialCoordinates;
}
