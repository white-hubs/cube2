package com.guhecloud.cube.biz.emergency.server.rule.domain.eumeration;

import lombok.Getter;

@Getter
public enum EmergencyConditionTimeTypeEnum {

    /**
     * 全天
     */
    ALL,

    /**
     * 周
     */
    WEEK,

    /**
     * 月
     */
    MONTH,

    /**
     * 时间段(时分秒)
     */
    TIME
}
