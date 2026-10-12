package com.guhecloud.cube.iot.mgt.dcm;

/**
 * 暂时存放在这儿，后续移除
 */
public enum IotProtocolEnum {
    gh_iot_protocol(1, "古河协议"),
    ali_iot_protocol(2, "阿里协议"),
    things_board_bl_protocol(3, "钡铼thingsBoard协议");

    private int id;
    private String label;

    IotProtocolEnum(int id, String label) {
        this.id = id;
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public int getId() {
        return id;
    }


    public static IotProtocolEnum getById(int id) {
        for (IotProtocolEnum protocolEnum : values()) {
            if (protocolEnum.getId() == id) {
                return protocolEnum;
            }
        }
        throw new IllegalArgumentException("No matching IotProtocolEnum for [" + id + "]");
    }

    public static IotProtocolEnum getByName(String name) {
        for (IotProtocolEnum protocolEnum : values()) {
            if (protocolEnum.name().equals(name)) {
                return protocolEnum;
            }
        }
        throw new IllegalArgumentException("No matching IotProtocolEnum for [" + name + "]");
    }
}
