package com.guhecloud.cube.biz.emergency.server.rule.domain.valueoabject;

import com.guhecloud.cube.biz.emergency.server.rule.domain.eumeration.EmergencyConditionTimeTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author lanchun
 * @date 2023/6/6 9:56
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EmergencyRuleTimeInfo {

    private Long id;

    /**
     * 时段类型
     */
    private EmergencyConditionTimeTypeEnum timeType;

    /**
     * 时间条件
     */
    private String timeConditions;

    public void setId(Long id) {
        this.id = id;
    }

    public void setTimeType(EmergencyConditionTimeTypeEnum timeType) {
        this.timeType = timeType;
    }

    public void setTimeConditions(String timeConditions) {
        this.timeConditions = timeConditions;
    }
}
