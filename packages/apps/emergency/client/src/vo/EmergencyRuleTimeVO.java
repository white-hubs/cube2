package com.guhecloud.cube.biz.emergency.client.vo;

import lombok.Data;

@Data
public class EmergencyRuleTimeVO {

    private Long id;

    private Long groupId;

    private String timeType;

    private String timeConditions;
}
