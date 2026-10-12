package com.guhecloud.cube.srv.gen.maintain.server.fault.enumeration;

import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.Getter;
import lombok.NonNull;

/**
 * @author wql
 * @describe 紧急程度
 * @date 2023/11/7 15:39
 */
@Getter
public enum UrgencyLevelEnum {

    NORMAL("一般"),

    IMPORTANCE("重要"),

    URGENCY("紧急");

    private final String desc;

    UrgencyLevelEnum(@NonNull String desc) {
        this.desc = desc;
    }

    public static String getDescription(String name) {
        if (ObjectUtils.isNotEmpty(name)) {
            for (UrgencyLevelEnum value : UrgencyLevelEnum.values()) {
                if (value.name().equals(name)) {
                    return value.getDesc();
                }
            }
        }
        return "";
    }
}
