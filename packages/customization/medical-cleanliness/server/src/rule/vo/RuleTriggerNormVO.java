package com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 规则触发指标信息
 *
 * @author lanchun
 * @date 2024/9/19 9:59
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RuleTriggerNormVO {

    private Long id;

    /**
     * 上报时间
     */
    private LocalDateTime uploadTime;

    /**
     * 指标值
     */
    private String normValue;

    /**
     * 指标值描述
     */
    private String normValueDescription;

    /**
     * 上限值
     */
    private BigDecimal upperLimit;

    /**
     * 下限值
     */
    private BigDecimal lowerLimit;
    /**
     * 指标名称
     */
    private String normName;
    private String normCode;
    private String unit;
}
