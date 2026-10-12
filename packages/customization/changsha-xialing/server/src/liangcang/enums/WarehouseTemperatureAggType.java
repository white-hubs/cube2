package com.guhecloud.cube.customization.xialing.server.liangcang.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/** 粮温汇总取值规则。 */
public enum WarehouseTemperatureAggType {
    /** 最高温。 */
    MAX("MAX"),
    /** 最低温。 */
    MIN("MIN");

    @EnumValue
    private final String code;

    WarehouseTemperatureAggType(String code) {
        this.code = code;
    }

    @JsonValue
    public String getCode() {
        return code;
    }

    @JsonCreator
    public static WarehouseTemperatureAggType fromCode(String code) {
        if (code == null) {
            return null;
        }
        for (WarehouseTemperatureAggType item : values()) {
            if (item.code.equals(code.trim())) {
                return item;
            }
        }
        throw new IllegalArgumentException("Unsupported warehouse temperature aggregate type: " + code);
    }
}
