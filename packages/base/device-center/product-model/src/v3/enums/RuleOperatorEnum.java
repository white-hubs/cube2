package com.guhecloud.cube.base.iot.product.model.v3.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 事件规则运算符枚举
 *
 * 定义事件规则中支持的比较运算符
 */
@Getter
@AllArgsConstructor
public enum RuleOperatorEnum {

    // ========== 比较运算符 ==========

    /**
     * 大于
     */
    GT("GT", "大于"),

    /**
     * 大于等于
     */
    GE("GE", "大于等于"),

    /**
     * 小于
     */
    LT("LT", "小于"),

    /**
     * 小于等于
     */
    LE("LE", "小于等于"),

    /**
     * 等于
     */
    EQ("EQ", "等于"),

    /**
     * 不等于
     */
    NE("NE", "不等于"),

    // ========== 集合运算符 ==========

    /**
     * 包含于集合
     */
    IN("IN", "包含于集合"),

    /**
     * 不在集合中
     */
    NOT_IN("NOT_IN", "不在集合中"),

    // ========== 范围运算符 ==========

    /**
     * 在区间范围内
     */
    BETWEEN("BETWEEN", "在区间范围内");

    /**
     * 运算符编码
     */
    @JsonValue
    private final String code;

    /**
     * 运算符描述
     */
    private final String desc;

    /**
     * 根据code获取枚举
     *
     * @param code 运算符编码
     * @return 对应的枚举值,如果不存在则返回null
     */
    public static RuleOperatorEnum fromCode(String code) {
        if (code == null) {
            return null;
        }
        for (RuleOperatorEnum operator : values()) {
            if (operator.code.equals(code)) {
                return operator;
            }
        }
        return null;
    }
}
