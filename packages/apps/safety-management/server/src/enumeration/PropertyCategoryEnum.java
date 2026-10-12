package com.guhecloud.cube.srv.gen.safety.management.enumeration;

import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.Getter;
import lombok.NonNull;

/**
 * @author wql
 * @describe 常量参数
 * @date 2023/9/13 19:29
 */
@Getter
public enum PropertyCategoryEnum {
    FIRE_FIGHTING_EQUIPMENT("消防设备"),
    TRANSPORT_DEVICES("现场运输设备"),
    FLAMMABLE_EQUIPMENT("易燃设备"),
    ELECTRO_MOBILE("电动车"),
    OXYGEN_CYLINDER("氧气瓶");

    private final String desc;

    PropertyCategoryEnum(@NonNull String desc) {
        this.desc = desc;
    }

    public static String getDescription(String name) {
        if (ObjectUtils.isEmpty(name)) {
            return "";
        }
        for (PropertyCategoryEnum value : PropertyCategoryEnum.values()) {
            if (value.name().equals(name)) {
                return value.desc;
            }
        }
        return PropertyCategoryEnum.FIRE_FIGHTING_EQUIPMENT.desc;
    }

}
