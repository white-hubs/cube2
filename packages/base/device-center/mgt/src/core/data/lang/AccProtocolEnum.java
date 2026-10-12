package com.guhecloud.cube.iot.mgt.core.data.lang;

/**
 * 平台接入适配协议
 */
public enum AccProtocolEnum {
    GH_IOT_PROTOCOL(1, "gh_iot_protocol", "古河协议"),
    ALI_IOT_PROTOCOL(2, "ali_iot_protocol", "阿里协议"),
    THINGS_BOARD_BL_PROTOCOL(3, "things_board_bl_protocol", "钡铼thingsBoard协议"),
    GH_CONVERT_PLATFORM_PROTOCOL(4, "gh_convert_protocol", "古河转换平台协议");

    public final long id;
    public final String code;
    public final String protocolName;

    AccProtocolEnum(long id, String code, String protocolName) {
        this.id = id;
        this.code = code;
        this.protocolName = protocolName;
    }

    public String accCode() {
        return code;
    }

    public static String idToName(long id) {
        for (AccProtocolEnum value : AccProtocolEnum.values()) {
            if (value.id == id) {
                return value.protocolName;
            }
        }

        throw new IllegalArgumentException("invalid id: " + id);
    }

    public static long nameToId(String name) {
        for (AccProtocolEnum value : AccProtocolEnum.values()) {
            if (value.protocolName.equals(name)) {
                return value.id;
            }
        }

        throw new IllegalArgumentException("invalid name: " + name);
    }

    public static AccProtocolEnum idToEnum(long id) {
        for (AccProtocolEnum value : AccProtocolEnum.values()) {
            if (value.id == id) {
                return value;
            }
        }

        throw new IllegalArgumentException("invalid id: " + id);
    }
}
