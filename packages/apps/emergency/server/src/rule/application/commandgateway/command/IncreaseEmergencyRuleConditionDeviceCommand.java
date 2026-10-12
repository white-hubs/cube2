package com.guhecloud.cube.biz.emergency.server.rule.application.commandgateway.command;

import lombok.Data;

/**
 * @author lanchun
 * @date 2022/9/3 14:24
 */
@Data
public class IncreaseEmergencyRuleConditionDeviceCommand {

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

    /**
     * 是否开启
     */
    private Boolean enabled;
}
