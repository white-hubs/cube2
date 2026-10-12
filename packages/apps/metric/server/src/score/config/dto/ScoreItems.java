package com.guhecloud.cube.biz.metric.server.score.config.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 分项配置
 *
 * @author lanchun
 * @date 2025/6/19 20:12
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScoreItems {

    /**
     * 项编号
     */
    private String itemCode;

    /**
     * 项名称
     */
    private String itemName;

    /**
     * 项类型
     */
    private String itemType;

    /**
     * 权重
     */
    private Double weightAllot;

    /**
     * 分项code编号
     */
    private String itemCodeConcat;

    /**
     * 计算指标
     */
    private Long computeMetric;

    /**
     * 规则内容模版
     */
    private String ruleContentTemplate;

    /**
     * 模版指标变量keys
     */
    private String templateMetricVariables;

    /**
     * 系数区间
     */
    private List<ScoreRange> coefficients;

    /**
     * 子级记分项
     */
    private List<ScoreItems> children;
}
