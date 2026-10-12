package com.guhecloud.cube.customized.youxianqu.park.management.server.estate.enums;

import lombok.Getter;

/**
 * 查询条件类型
 */
@Getter
public enum ConditionTypeEnum {
    GT("gt;", ">", "大于"),
    GE("gt;=", ">=", "大于等于"),
    EQ("=", "=", "等于"),
    LE("lt;=", "<=", "小于等于"),
    LT("lt;", "<", "小于"),
    ;
    final String code;
    final String symbol;
    final String describe;

    ConditionTypeEnum(String code, String symbol, String describe) {
        this.code = code;
        this.symbol = symbol;
        this.describe = describe;
    }
}
