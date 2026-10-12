package com.guhecloud.cube.base.device.center.core.dto;

import lombok.Data;

@Data
public class DeviceBizIotDTO {
    /**
     * 物联网设备ID
     */
    private Long id;

    /**
     * 物联网设备编码
     */
    private String iotDeviceCode;

    /**
     * 物联网设备型号path
     */
    private String iotDeviceNumberPath;

    /**
     * 物联网设备型号名称
     */
    private String iotDeviceModelName;

    /**
     * 物联网设备型号code
     */
    private String iotDeviceNumberCode;

    /**
     * 设备品牌path
     */
    private String iotDeviceBrandPath;

    /**
     * 设备品牌名称
     */
    private String iotDeviceBrandName;

    /**
     * 设备品牌code
     */
    private String iotDeviceBrandCode;

    /**
     * 物联网设备产品ID
     */
    private Long iotDeviceProductId;

    /**
     * 物联网设备产品名称
     */
    private String iotDeviceProductName;
}
