package com.guhecloud.cube.srv.gen.energymanagement.server.enumeration;

import lombok.Getter;

/**
 * @author wql
 * @describe 负荷预测查询时间单位
 * @date 2023/4/10 19:22
 */
@Getter
public enum QueryTimeUnitEnum {

    TODAY("TODAY", "日"),
    THIS_MONTH("THIS_MONTH", "月"),
    THIS_YEAR("THIS_YEAR", "年");

    private final String code;
    private final String name;

    QueryTimeUnitEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public static String getByCode(String code) {
        for (QueryTimeUnitEnum value : QueryTimeUnitEnum.values()) {
            if (value.code.equals(code)) {
                return value.name;
            }
        }
        return "";
    }
}
