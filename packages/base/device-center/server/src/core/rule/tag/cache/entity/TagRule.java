package com.guhecloud.cube.base.device.center.core.rule.tag.cache.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TagRule {

    /**
     * 标签编号
     */
    private String code;

    /**
     * 规则
     */
    private String rule;

    /**
     * 是否存在更新
     */
    private Boolean updated;

    /**
     * 更新时间
     */
    private Long updateTime;

    /**
     * 租户编号
     */
    private Integer tenant;
}
