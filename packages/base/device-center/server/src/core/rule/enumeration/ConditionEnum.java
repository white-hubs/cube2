package com.guhecloud.cube.base.device.center.core.rule.enumeration;

import lombok.Getter;

@Getter
public enum ConditionEnum {
    GREATER_THAN(">"), // 大于
    GREATER_THAN_OR_EQUAL(">="), // 大于等于
    LESS_THAN("<"), // 小于
    LESS_THAN_OR_EQUAL("<="), // 小于等于
    IN("in"), // 包含
    LIKE("like"), // 模糊匹配
    NOT_IN("not in"), //不包含
    EQ("eq"); //等于

    private final String symbol;

    ConditionEnum(String symbol) {this.symbol = symbol;}
}
