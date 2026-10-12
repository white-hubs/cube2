package com.guhecloud.cube.customized.youxianqu.park.management.server.investmentplan.enums;

import lombok.Getter;

/**
 * 招商计划目标类型
 */
@Getter
public enum PlanTargetTypeEnum {
    BUY_LAND(1, "购地"),
    LEASE(2, "租赁"),
    SALE(3, "销售"),
    ;
    final Integer code;
    final String describe;

    PlanTargetTypeEnum(Integer code, String describe) {
        this.code = code;
        this.describe = describe;
    }
}
