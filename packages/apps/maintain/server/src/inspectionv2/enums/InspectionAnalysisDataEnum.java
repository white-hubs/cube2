package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.enums;

import lombok.Getter;

/**
 * @author wql
 * @describe 巡检分析时间类型枚举
 * @date 2023/4/18 10:02
 */
@Getter
public enum InspectionAnalysisDataEnum {
    ToDay("今日"),
    ThisWeek("本周"),
    ThisMonth("本月"),
    ThisYear("今年");

    private final String describe;

    InspectionAnalysisDataEnum(String describe) {
        this.describe = describe;
    }

}
