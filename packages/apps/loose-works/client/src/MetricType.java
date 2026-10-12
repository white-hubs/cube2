package com.guhecloud.cube.srv.gen.loose.works;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/6/6 11:52
 * @Description:
 */
public enum MetricType {
    /**
     * 总数
     */
    TOTAL_COUNT("总数"),

    /**
     * 完成率
     */
    COMPLETED_RATE("完成率"),

    /**
     * 异常率
     */
    ABNORMAL_RATE("异常率"),

    /**
     * 整改率
     */
    REFORM_RATE("整改率"),

    /**
     * 整改效率
     */
    REFORM_EFFICIENCY("整改效率"),

    /**
     * 验收率
     */
    ACCEPTANCE_RATE("验收率"),

    /**
     * 全量指标
     */
    ALL("全量指标");
    private final String desc;

    MetricType(String desc) {
        this.desc = desc;
    }
}
