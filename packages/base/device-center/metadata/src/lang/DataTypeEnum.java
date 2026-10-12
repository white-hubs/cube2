package com.guhecloud.cube.base.device.center.metadata.lang;

import com.guhecloud.cube.base.device.center.metadata.annotation.VersionInfo;
import lombok.Getter;

@Getter
public enum DataTypeEnum {

    BOOLEAN("boolean", "布尔型"),
    FLOAT("float", "浮点型"),
    ENUM("enum", "枚举型"),
    INTEGER("int", "数字类型"),
    STRING("string", "字符串"),
    DOUBLE("double", "双精度"),
    @VersionInfo(availableSince = Version.V_2_0_1)
    LONG("long", "长整型");

    final String value;
    final String name;

    DataTypeEnum(String value, String name) {
        this.value = value;
        this.name = name;
    }

    public static DataTypeEnum convertByCode(String dataType) {
        for (DataTypeEnum val : values()) {
            if (val.value.equalsIgnoreCase(dataType)) {
                return val;
            }
        }

        throw new IllegalArgumentException("不支持的数据类型" + dataType);
    }
}
