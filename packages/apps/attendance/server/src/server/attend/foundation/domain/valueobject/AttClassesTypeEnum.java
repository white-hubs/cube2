package com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.domain.valueobject;

import com.guhecloud.platform.base.util.StringUtils;
import lombok.Getter;

/**
 * 班次类型
 */
@Getter
public enum AttClassesTypeEnum {
    /**
     * 行政班
     */
    ADMINISTRATION_SHIFT("行政班"),
    /**
     * 早班
     */
    EARLY("早班"),
    /**
     * 中班
     */
    MIDDLE_SHIFT("中班"),
    /**
     * 夜班
     */
    NIGHT_SHIFT("夜班"),

    REST("休息"),
    ;

    private final String value;

    AttClassesTypeEnum(String value) {
        this.value = value;
    }

    public static String getTypeValue(String name) {
        if (StringUtils.isBlank(name)) {
            return "";
        }
        for (AttClassesTypeEnum typeEnum : AttClassesTypeEnum.values()) {
            if (typeEnum.name().equals(name)) {
                return typeEnum.getValue();
            }
        }
        return "";
    }
}
