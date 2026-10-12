package com.guhecloud.cube.srv.gen.securitymanagement.server.common.param;

import lombok.Data;

@Data
public class SecurityDeviceParam {
    //设备名称
    private String deviceName;
    //设备id
    private Long deviceId;
    //设备code
    private String code;
    //模块
    private String applicationCode;
    //区域id
    private String areaId;
    //区域名称
    private String areaName;

    //品牌
    private String brandName;
    /**
     * 第三方设备id
     */
    private String thirdPartyId;

    /**
     * 经纬度
     */
    private String lntAndLat;

    //状态
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
