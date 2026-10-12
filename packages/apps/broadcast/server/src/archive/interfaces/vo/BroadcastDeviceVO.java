package com.guhecloud.cube.biz.subsystem.broadcast.server.archive.interfaces.vo;

import lombok.Data;

@Data
public class BroadcastDeviceVO {
    private Long id;
    /**
     * 设备名称
     */
    private String deviceName;

    /**
     * 设备id
     */
    private Long deviceId;

    /**
     * 应用编号
     */
    private String applicationCode;

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

    /**
     * 品牌
     */
    private String brandName;

    /**
     * 区域名称
     */
    private String areaName;

    /**
     * 区域
     */
    private String areaId;

    /**
     * 编码
     */
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
     * 工作状态(0:空闲,1:占用)
     */
    private String workStatus;

}
