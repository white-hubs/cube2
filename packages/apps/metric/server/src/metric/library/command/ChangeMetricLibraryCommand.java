package com.guhecloud.cube.biz.metric.server.metric.library.command;

import com.guhecloud.cube.biz.metric.server.metric.library.enumeration.MetricComputeType;
import lombok.Data;

@Data
public class ChangeMetricLibraryCommand {

    /**
     * 主键
     */
    private Long id;

    /**
     * 指标名称
     */
    private String name;

    /**
     * 计算方式
     */
    private MetricComputeType computeType;

    /**
     * 指标计算表达式，当计算方式为多项指标组合结果时使用
     */
    private String computeFormula;

    /**
     * 参与计算的指标id集合(多个用','隔开)
     */
    private String computeFormulaMetrics;
}
