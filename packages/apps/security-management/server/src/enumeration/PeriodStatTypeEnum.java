package com.guhecloud.cube.srv.gen.securitymanagement.server.enumeration;

import lombok.Getter;
import lombok.NonNull;

@Getter
public enum PeriodStatTypeEnum {

    TODAY("今日", "yyyy-MM-dd HH"),
    THIS_WEEK("本周", "yyyy-MM-dd"),
    THIS_MONTH("本月", "yyyy-MM-dd"),
    THIS_YEAR("本年", "yyyy-MM"),
    ;
    public final String label;
    public final String dateFormat;

    PeriodStatTypeEnum(@NonNull String label, @NonNull String dateFormat) {
        this.label = label;
        this.dateFormat = dateFormat;
    }
}
