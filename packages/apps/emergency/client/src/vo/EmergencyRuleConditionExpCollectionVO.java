package com.guhecloud.cube.biz.emergency.client.vo;

import lombok.Data;

@Data
public class EmergencyRuleConditionExpCollectionVO {

    private Long id;

    private Long emergencyRuleId;

    private Long emergencyRuleConditionId;

    private Long groupId;

    private String propCode;

    private String propName;

    private String propUnit;

    private String op;

    private String oprand;

    private Integer delayTime;

    private Integer startDelayTime;

    private Integer endDelayTime;
}
