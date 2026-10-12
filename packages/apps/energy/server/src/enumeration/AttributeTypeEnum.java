package com.guhecloud.cube.srv.gen.energymanagement.server.enumeration;

import lombok.Getter;

@Getter
public enum AttributeTypeEnum {
    WATER_METER("sds", "water_meter"),
    ELECTRICITY_METER("dds", "electricity_meter"),
    GAS_METER("qds", "gas_meter"),
    FUEL_METER("fds", "fuel_meter"),
    HEAT_METER("hds", "heat_meter"),
    COOL_METER("cds", "cool_meter"),
    RENEWABLE_ENERGY("reds", "renewable_energy"),
    OTHER("ods", "other"),
    VAPOUR("rds", "vapour"),//蒸汽表
    POWER_TRANSFORMATION_DISTRIBUTION("watersupply_tp,backwater_tp,actually_flow_rate", "energy_meter");

    private final String code;
    private final String des;

    AttributeTypeEnum(String code, String des) {
        this.code = code;
        this.des = des;
    }

    public static String getByName(String name) {
        for (AttributeTypeEnum value : AttributeTypeEnum.values()) {
            if (value.name().equals(name)) {
                return value.code;
            }
        }

        return "";
    }

    public static String getByDesc(String des) {
        for (AttributeTypeEnum value : AttributeTypeEnum.values()) {
            if (value.des.equals(des)) {
                return value.code;
            }
        }

        return "";
    }
}
