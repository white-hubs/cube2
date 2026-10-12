package com.guhecloud.cube.biz.emergency.dcm.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.guhecloud.cube.biz.emergency.server.rule.domain.eumeration.EmergencyConditionTimeTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class DcmEmergencyRuleTime {

    /**
     * 时段类型
     */
    private EmergencyConditionTimeTypeEnum timeType;

    /**
     * 时间条件
     */
    private String timeConditions;
}
