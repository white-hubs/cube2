package com.guhecloud.cube.biz.subsystem.broadcast.server.rule.param;

import lombok.Data;

@Data
public class BroadcastPlayRuleDeviceModifyParam {

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
     * 设备主键
     */
    private Long deviceId;

    /**
     * 设备名称
     */
    private String deviceName;

    /**
     * 所属区域
     */
    private String deviceAreaId;

    /**
     * 区域名称
     */
    private String deviceAreaName;
}
