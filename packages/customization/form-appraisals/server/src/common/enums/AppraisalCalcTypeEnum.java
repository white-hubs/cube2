package com.guhecloud.cube.srv.gen.appraisals.server.common.enums;

/**
 * 考核计算类型枚举
 * <p>
 * 定义考核指标的不同计算方式，包括表单计数、字段值获取、字段统计（求和、平均值）、
 * 异常项计数以及任务相关计数（总任务数、超时任务数）等多种计算类型。
 * </p>
 *
 * @author system
 */
public enum AppraisalCalcTypeEnum {
    /**
     * 表单计数 - 统计符合条件的表单数量
     */
    FORM_COUNT,

    /**
     * 字段值 - 直接获取指定字段的值作为考核结果
     */
    FIELD_VALUE,

    /**
     * 字段求和 - 对指定字段的所有值进行求和计算
     */
    FIELD_SUM,

    /**
     * 字段平均值 - 计算指定字段所有值的平均数
     */
    FIELD_AVG,

    /**
     * 异常项计数 - 统计异常项目的数量
     */
    ABNORMAL_ITEM_COUNT,

    /**
     * 任务计数 - 统计任务的总数量
     */
    TASK_COUNT,

    /**
     * 超时任务计数 - 统计超时的任务数量
     */
    TIMEOUT_TASK_COUNT
}
