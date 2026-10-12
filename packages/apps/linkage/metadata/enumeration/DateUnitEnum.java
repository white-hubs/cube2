package com.guhecloud.apps.linkage.metadata.enumeration;

/**
 * 周期单位
 *
 * @author lanchun
 * @date 2022/6/24 14:53
 */
public enum DateUnitEnum {
    SECOND("秒"),
    MINUTE("分钟"),
    HOUR("小时"),
    DAY("天"),
    MONTH("月");

    /**
     * 描述信息
     */
    private final String description;

    DateUnitEnum(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
