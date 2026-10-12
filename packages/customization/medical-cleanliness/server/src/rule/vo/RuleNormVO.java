package com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author: lijie
 * @CreateTime: 2024-03-20  16:40
 * @Description:
 * @Version: 1.0
 */
@Data
public class RuleNormVO {
    private Long id;
    /**
     * 规则id
     */
    private Long ruleId;
    /**
     * 指标名称
     */
    private String normName;
    private String normCode;
    /**
     * 单位
     */
    private String unit;
    /**
     * 是否展示
     */
    private Integer display;
    /**
     * 上限
     */
    private BigDecimal upperLimit;
    /**
     * 下限
     */
    private BigDecimal lowerLimit;
    /**
     * 展示区域
     */
    private String showArea;
    /**
     * 排序
     */
    private Integer sort;
    private String deviceids;
    private String prodId;
    private Integer tenant;
}
