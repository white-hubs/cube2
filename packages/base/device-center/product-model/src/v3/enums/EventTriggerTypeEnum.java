package com.guhecloud.cube.base.iot.product.model.v3.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 事件触发类型枚举
 *
 * 定义产品事件的触发方式
 */
@Getter
@AllArgsConstructor
public enum EventTriggerTypeEnum {

    /**
     * 原生事件：设备主动上报的事件
     */
    NATURE("NATURE", "原生事件"),

    /**
     * 简单规则事件：通过本地规则生成的事件
     */
    SIMPLE("SIMPLE", "简单规则事件");

    /**
     * 类型编码
     */
    @JsonValue
    private final String code;

    /**
     * 类型描述
     */
    private final String desc;

    /**
     * 根据code获取枚举
     *
     * @param code 类型编码
     * @return 对应的枚举值,如果不存在则返回null
     */
    public static EventTriggerTypeEnum fromCode(String code) {
        if (code == null) {
            return null;
        }
        for (EventTriggerTypeEnum type : values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        return null;
    }
}
