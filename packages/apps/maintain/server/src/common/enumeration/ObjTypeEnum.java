package com.guhecloud.cube.srv.gen.maintain.server.common.enumeration;

import lombok.Getter;

/**
 * @author wql
 * @describe 对象类型
 * @date 2023/10/31 10:40
 */
@Getter
public enum ObjTypeEnum {
    STALL("STALL", "档位"),
    DEVICE("DEVICE", "设备"),
    PROPERTY("PROPERTY", "资产设备"),
    SITE_POINT("SITE_POINT", "现场点"),
    PATROL_POINT("PATROL_POINT", "巡更点"),
    RISK_SOURCE("RISK_SOURCE", "风险源"),
    SMALL_ENGINEERING("SMALL_ENGINEERING", "小散工程"),
    CUSTOM("CUSTOM", "自定义"),
    ;

    private final String code;
    private final String name;

    ObjTypeEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public static String getNameByCode(String code) {
        for (ObjTypeEnum value : ObjTypeEnum.values()) {
            if (code.equals(value.code)) {
                return value.name;
            }
        }
        return null;
    }
}
