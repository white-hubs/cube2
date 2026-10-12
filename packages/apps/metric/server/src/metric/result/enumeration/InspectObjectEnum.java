package com.guhecloud.cube.biz.metric.server.metric.result.enumeration;

import lombok.Getter;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/6/21 17:33
 * @Description:
 */
@Getter
public enum InspectObjectEnum {
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
    ;

    /**
     * 描述信息
     */
    private final String description;

    InspectObjectEnum(String description) {
        this.description = description;
    }

}
