package com.guhecloud.cube.base.device.center.metadata.vo;

import com.guhecloud.cube.base.device.center.metadata.lang.DeviceInstanceStatusEnum;
import lombok.Data;

@Data
public class DeviceDetailThinVO {
    private Long deviceId;

    /**
     * 设备编码
     */
    private String deviceCode;

    /**
     * 设备名称
     */
    private String deviceName;

    /**
     * 品牌code
     */
    private String brandCode;

    private String brandName;

    /**
     * 物联网设备产品ID
     */
    private Long iotDeviceProductId;

    /**
     * 物联网设备产品名称
     */
    private String iotDeviceProductName;

    /**
     * 类型id
     */
    private Long genreId;

    private String genreName;
    //类型标识
    private String genreCode;

    /**
     * 区域id
     */
    private String areaId;
    private String areaName;
    private Long singleAreaId;

    private String departmentId;
    private String departmentName;

    /**
     * 经纬度
     */
    private String lntAndLat;
    /**
     * 第三方设备标识
     */
    private String thirdPartyId;
    /**
     * 三方设备配置
     */
    private String thirdpartyCfg;

    /**
     * 0 离线 1在线
     */
    private int deviceState;

    /**
     * 设备状态
     */
    private DeviceInstanceStatusEnum deviceStatus;

    //通信状态,中文描述
    private String communicationStatus;
}
