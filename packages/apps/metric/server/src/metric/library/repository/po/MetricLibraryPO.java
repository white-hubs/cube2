package com.guhecloud.cube.biz.metric.server.metric.library.repository.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.cube.biz.metric.server.metric.library.enumeration.MetricComputeType;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("metric_library")
public class MetricLibraryPO extends BasicEnhancedTenantEntity<Long> {

    private Long id;

    /**
     * 指标编号
     */
    private String code;

    /**
     * 指标名称
     */
    private String name;

    /**
     * 计算方式
     */
    private MetricComputeType computeType;

    /**
     * 是否为初始化的
     */
    private Boolean initialized;

    /**
     * 指标计算表达式，当计算方式为多项指标组合结果时使用
     */
    private String computeFormula;

    /**
     * 参与计算的指标id集合(多个用','隔开)
     */
    private String computeFormulaMetrics;

    /**
     * 后端生成的执行表达式（含嵌套解析）
     */
    private String executableFormula;

    /**
     * 所有基础指标ID（递归展开后，如"1,2,456"）
     */
    private String allBaseMetricIds;
}
