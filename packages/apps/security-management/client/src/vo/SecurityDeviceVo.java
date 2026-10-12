package com.guhecloud.cube.srv.gen.securitymanagement.vo;

import lombok.Data;

@Data
public class SecurityDeviceVo {
    private Long id;
    //设备名称
    private String deviceName;
    //设备id
    private Long deviceId;
    //模块
    private String applicationCode;
    //品牌
    private String brandName;
    //品牌
    private String brandCode;

    //区域
    private String areaName;
    //区域
    private String areaId;

    //编码
    private String code;

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
