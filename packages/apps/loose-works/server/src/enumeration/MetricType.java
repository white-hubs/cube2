package com.guhecloud.cube.srv.gen.loose.works.enumeration;

import lombok.Getter;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/6/6 11:44
 * @Description: 指标类型
 */
@Getter
public enum MetricType {
    TOTAL_COUNT("总数"),
    COMPLETED_RATE("完成率"),
    ABNORMAL_RATE("异常率"),
    REFORM_RATE("整改率"),
    REFORM_EFFICIENCY("整改效率"),
    ACCEPTANCE_RATE("验收率"),
    ;
    private final String desc;

    MetricType(String desc) {
        this.desc = desc;
    }
}
