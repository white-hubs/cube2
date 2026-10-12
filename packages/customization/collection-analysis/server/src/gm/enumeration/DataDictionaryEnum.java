package com.guhecloud.cube.biz.collection.analysis.gm.enumeration;

import lombok.Getter;

/**
 * @Author: lijie
 * @CreateTime: 2024-12-16  11:28
 * @Description:
 * @Version: 1.0
 */
@Getter
public enum DataDictionaryEnum {
    ONLINE_SECURITY_CHECK("近一周食安检测次数"),
    PARK_RISK__RANKING("风险产品预警排名"),
    PARK_SETTLE_CATEGORY_DAY("昨日来货统计"),
    PARK_SETTLE_CATEGORY_WEEK("近一周来货趋势"),
    PARK_SETTLE_VEHICLE_WEEK("车辆统计、近一周来货&采购车辆趋势图"),
    PARK_FOOD_SAFE_DAY("昨日食安检测统计"),
    PARK_STAFF_WORK_DAY("昨日转运统计"),
    PARK_STAFF_WORK_WEEK("近一周转运统计"),
    PARK_LOADING_WEEK("近一周装货车辆统计"),
    PARK_TRAFFIC_STATISTIC_INFO("近一日车流分布图");

    private final String remarks;

    DataDictionaryEnum(String remarks) {
        this.remarks = remarks;
    }
}
