package com.guhecloud.cube.customization.xialing.server.liangcang.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/** 粮温趋势统计范围。 */
public enum WarehouseTemperatureTrendScope {
    /** 全仓。 */
    ALL("ALL"),
    /** 上层。 */
    UPPER("UPPER"),
    /** 中层。 */
    MIDDLE("MIDDLE"),
    /** 下层。 */
    LOWER("LOWER");

    private final String code;

    WarehouseTemperatureTrendScope(String code) {
        this.code = code;
    }

    @JsonValue
    public String getCode() {
        return code;
    }

    @JsonCreator
    public static WarehouseTemperatureTrendScope fromCode(String code) {
        if (code == null) {
            return null;
        }
        for (WarehouseTemperatureTrendScope item : values()) {
            if (item.code.equals(code.trim())) {
                return item;
            }
        }
        throw new IllegalArgumentException("Unsupported warehouse temperature trend scope: " + code);
    }
}
