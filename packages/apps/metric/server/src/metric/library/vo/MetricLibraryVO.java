package com.guhecloud.cube.biz.metric.server.metric.library.vo;

import com.guhecloud.cube.biz.metric.server.metric.library.enumeration.MetricComputeType;
import com.guhecloud.platform.base.model.ActorTypeEnum;
import lombok.Data;

@Data
public class MetricLibraryVO {

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
     * 指标计算表达式
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

    /**
     * 创建人id
     */
    private String createBy;

    /**
     * 创建人主键
     */
    private String creatorId;

    /**
     *
     */
    private ActorTypeEnum creatorType;

    /**
     * 创建人名称
     */
    private String createByName;

    /**
     * 创建时间
     */
    private String createTime;
}
