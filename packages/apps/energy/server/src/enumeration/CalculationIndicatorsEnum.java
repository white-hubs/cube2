package com.guhecloud.cube.srv.gen.energymanagement.server.enumeration;

import lombok.Getter;

@Getter
public enum CalculationIndicatorsEnum {
    WATER_METER("water_meter", "0.0857"),
    ELECTRICITY_METER("electricity_meter", "0.32"),
    GAS_METER("gas_meter", "0.714"),
    VAPOUR("vapour", "97.14");

    private final String code;
    private final String name;

    CalculationIndicatorsEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public static String getIndicatorsEnum(String code) {
        for (CalculationIndicatorsEnum fireinspEnum : CalculationIndicatorsEnum.values()) {
            if (fireinspEnum.getCode().equals(code)) {
                return fireinspEnum.getName();
            }
        }
        return "";
    }

}
