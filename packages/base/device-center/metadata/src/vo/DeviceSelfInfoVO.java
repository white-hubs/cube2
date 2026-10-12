package com.guhecloud.cube.base.device.center.metadata.vo;

import lombok.Data;

/**
 * 说明此设备自包含一些必要的信息
 */
@Data
public class DeviceSelfInfoVO {
    //设备id
    private Long id;
    //编码
    private String deviceCode;
    //设备名称
    private String deviceName;
    /**
     * 物联网设备产品ID
     */
    private Long iotDeviceProductId;

    /**
     * 物联网设备产品名称
     */
    private String iotDeviceProductName;
    //设备类型
    private String genreName;
    //类型id
    private Long genre;
    //品牌
    private String brandName;
    //区域id
    private String areaId;
    //区域名称
    private String areaName;
    //类型标识
    private String genreCode;

    /**
     * 三方设备ID
     */
    private String thirdPartyId;

    /**
     * 三方设备配置
     */
    private String thirdpartyCfg;

    /**
     * 设备图片文件ID
     */
    private Long picture;

}
