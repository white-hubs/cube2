package com.guhecloud.cube.biz.emergency.client.vo;

import lombok.Data;

@Data
public class EmergencyRuleConditionDeviceCollectionVO {

    private Long id;

    private Long emergencyRuleId;

    private String deviceProdCategoryCode;

    private String deviceProdCategoryCodeConcat;

    private String deviceProdCategoryName;

    private Long deviceProdId;

    private String deviceProdName;

    private Long deviceId;

    private String deviceName;

    private String deviceAreaId;

    private String deviceAreaName;

    private Boolean enabled;
}
