package com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.param;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author: lijie
 * @CreateTime: 2024-03-20  16:41
 * @Description:
 * @Version: 1.0
 */
@Data
public class RuleNormParam {
    /**
     * 指标编码
     */
    private String normCode;
    /**
     * 指标名称
     */
    private String normName;
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
}
