package com.guhecloud.cube.biz.daily.task.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TaskTargetTypeEnum {
    /**
     * AREA
     */
    AREA("区域"),
    /**
     * 档位
     */
    STALL("档位"),

    /**
     * 设备
     */
    DEVICE("设备"),
    /**
     * 资产
     */
    PROPERTY("资产"),
    /**
     * 现场点
     */
    SITE_POINT("现场点"),
    /**
     * 风险源
     */
    RISK_SOURCE("风险源"),

    /**
     * 小散工程
     */
    SMALL_ENGINEERING("小散工程"),
    /**
     * 巡更点
     */
    PATROL_POINT("巡更点"),
    ;

    /**
     * 描述信息
     */
    private final String description;

}
