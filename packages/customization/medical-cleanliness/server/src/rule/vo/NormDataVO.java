package com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author: lijie
 * @CreateTime: 2024-03-22  09:48
 * @Description:
 * @Version: 1.0
 */
@Data
public class NormDataVO {
    private Long id;
    /**
     * 指标名称
     */
    private String normName;
    /**
     * 指标编码
     */
    private String normCode;
    /**
     * 指标单位
     */
    private String unit;
    /**
     * 指标状态:NORMAL("正常"),LOWER_LIMIT("下限"),UPPER_LIMIT("上限")
     */
    private String status;
    /**
     * 指标数值
     */
    private Integer value;
    /**
     * 是否展示
     */
    private Integer display;
    private BigDecimal upperLimit;
    private BigDecimal lowerLimit;
    private Integer sort;
    private String showArea;

}
