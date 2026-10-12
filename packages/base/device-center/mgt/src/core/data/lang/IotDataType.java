package com.guhecloud.cube.iot.mgt.core.data.lang;

import lombok.Getter;

/**
 * 数据类型,IOT 平台支持的
 */
@Getter
public enum IotDataType {

    INT("int32(整型)", "int"),
    LONG("int64(整型)", "long"),
    FLOAT("float(单精度)", "float"),
    DOUBLE("double(双精度)", "double"),
    BOOLEAN("boolean(布尔型)", "boolean"),
    STRING("string(字符类型）", "string"),
    ENUM("enum(枚举)", "enum");

    public final String label;
    public final String code;

    IotDataType(String label, String code) {
        this.label = label;
        this.code = code;
    }

    public static IotDataType fromCode(String code) {
        for (IotDataType val : values()) {
            if (val.getCode().equalsIgnoreCase(code)) {
                return val;
            }
        }

        throw new IllegalArgumentException("不支持的数据类型" + code);
    }

    public static void main(String[] args) {
        for (IotDataType value : IotDataType.values()) {
            System.out.print("\""+value.getCode()+"\",");
        }
    }
}
