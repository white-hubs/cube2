package com.guhecloud.cube.biz.emergency.client.vo;

import lombok.Data;

import java.util.List;

@Data
public class EmergencyRuleConditionCollectionVO {

    private Long id;

    private Long groupId;

    private Long emergencyRuleId;

    private String deviceProdCategoryCode;

    private String deviceProdCategoryCodeConcat;

    private String deviceProdCategoryName;

    private Long deviceProdId;

    private String deviceProdName;

    private String conditionType;

    private String deviceEventId;

    private String deviceEventCode;

    private String deviceEventName;

    private String deviceEventType;

    private String logicalOp;

    private String eventConfigType;

    private List<EmergencyRuleConditionExpCollectionVO> emergencyRuleConditionExps;
}
