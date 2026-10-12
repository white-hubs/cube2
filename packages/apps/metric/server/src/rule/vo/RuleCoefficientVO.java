package com.guhecloud.cube.biz.metric.server.rule.vo;

import lombok.Data;

/**
 * 分值区间
 *
 * @author lanchun
 * @date 2025/6/3 15:50
 */
@Data
public class RuleCoefficientVO {

    private Long id;

    /**
     * 大于
     */
    private Double greaterThan;

    /**
     * 小于等于
     */
    private Double lessThanOrEqual;

    /**
     * 得分占比
     */
    private Double scoreRatio;
}
