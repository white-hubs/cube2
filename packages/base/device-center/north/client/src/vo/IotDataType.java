package com.guhecloud.cube.iot.north.bridge.client.vo;

import lombok.Getter;

/**
 * 数据类型,IOT 平台支持的
 */
public enum IotDataType {

    INT("int32(整型)", "int"),
    LONG("int64(整型)", "long"),
    FLOAT("float(单精度)", "float"),
    DOUBLE("double(双精度)", "double"),
    BOOLEAN("boolean(布尔型)", "boolean"),
    STRING("string(字符类型）", "string"),
    ENUM("enum(枚举)", "enum");

    public final String chineseName;
    @Getter
    public final String code;

    IotDataType(String chineseName, String code) {
        this.chineseName = chineseName;
        this.code = code;
    }

    public String getLabel() {
        return chineseName;
    }

    /**
     * 根据code获取枚举
     *
     * @param code code
     * @return 枚举
     */
    public static IotDataType convertByCode(String code) {
        for (IotDataType val : values()) {
            if (val.code.equalsIgnoreCase(code)) {
                return val;
            }
        }

        throw new IllegalArgumentException("不支持的数据类型" + code);
    }
}
