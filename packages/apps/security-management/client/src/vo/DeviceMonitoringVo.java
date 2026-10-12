package com.guhecloud.cube.srv.gen.securitymanagement.vo;

import lombok.Data;

@Data
public class DeviceMonitoringVo {
    //设备id
    private Long deviceId;
    //设备
    private String deviceName;
    //设备编码
    private String deviceCode;
    //区域
    private String areaId;
    //区域
    private String areaName;

    //品牌
    private String brandName;

    /**
     * 经纬度
     */
    private String lntAndLat;

    /**
     * 第三方设备标识
     */
    private String thirdPartyId;

    /**
     * 设备状态
     */
    private Boolean deviceStatus;

    /**
     * 产品品类编号
     */
    private String deviceProdCategoryCode;

    /**
     * 产品品类编号序列号
     */
    private String deviceProdCategoryCodeConcat;

    /**
     * 产品品类名称
     */
    private String deviceProdCategoryName;

    /**
     * 设备产品主键
     */
    private Long deviceProdId;

    /**
     * 设备产品名称
     */
    private String deviceProdName;

}
