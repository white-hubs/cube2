package com.guhecloud.cube.srv.gen.safety.management.monthlyreport.enumeration;

import lombok.Getter;

@Getter
public enum MonthlyReportStatusEnum {
    NOT_STARTED("未完成"),

    COMPLETED("已完成填报");

    private String remarks;

    MonthlyReportStatusEnum(String remarks) {
        this.remarks = remarks;
    }
}
