package com.guhecloud.cube.biz.subsystem.broadcast.server.common.infrastruceture.facade.securitymanage.response;

import lombok.Data;

/**
 * 广播设备信息
 *
 * @author lanchun
 * @date 2023/11/7 15:06
 */
@Data
public class BroadcastDeviceResponse {

    private Long id;
    //设备名称
    private String deviceName;
    //设备id
    private Long deviceId;
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
}
