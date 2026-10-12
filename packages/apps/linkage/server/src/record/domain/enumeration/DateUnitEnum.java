package com.guhecloud.cube.biz.linkage.server.record.domain.enumeration;

import lombok.Getter;
import lombok.NonNull;

/**
 * 周期单位
 *
 * @author lanchun
 * @date 2022/6/24 14:53
 */
@Getter
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

    DateUnitEnum(@NonNull String description) {
        this.description = description;
    }
}
