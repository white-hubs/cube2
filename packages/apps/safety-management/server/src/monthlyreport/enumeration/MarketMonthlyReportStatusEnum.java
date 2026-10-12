package com.guhecloud.cube.srv.gen.safety.management.monthlyreport.enumeration;

import lombok.Getter;

/**
 * @Author: lijie
 * @CreateTime: 2023-09-07  17:15
 * @Description:
 * @Version: 1.0
 */
@Getter
public enum MarketMonthlyReportStatusEnum {
    NOT_STARTED("未完成"),
    ONGOING("已填报"),
    WIND_UP("已上报"),
    ;
    private String remarks;

    MarketMonthlyReportStatusEnum(String remarks) {
        this.remarks = remarks;
    }
}
