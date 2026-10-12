package com.guhecloud.cube.srv.gen.safety.management.report.enumeration;

import lombok.Getter;

@Getter
public enum SafetyReportEnum {
    CURRENT("电流报警", "current"),
    VOLTAGE("电压报警", "voltage"),
    TEMPERATURE("温度报警", "temperature"),
    OPEN_FIRE("明火报警", "openFire"),
    SMOKE("烟雾报警", "smoke"),
    VIOLATION_FIRE("违规占用消防通道", "violationFire"),
    HYDRAULIC_PRESSURE("水压报警", "hydraulicPressure"),
    HAND_REPORT("手报报警", "handReport"),
    WARM_SENSATION("温感报警", "warmSensation"),
    GAS_LEAKAGE("燃气泄漏", "gasLeakage"),
    PARKING_VIOLATION("车辆违停", "parking"),
    TRAFFIC_JAM("交通拥堵", "trafficJam"),
    OCCUPY_OPERATIONS("占道经营", "occupyOperations"),
    ELECTRIC_VEHICLES_ELEVATOR("电动车管理", "electricVehiclesElevator"),
    HUMIDITY("温湿度报警", "humidity"),
    UPS("UPS报警", "ups"),
    FIRE_FIGHTING_EQUIPMENT("消防设备", "fireDevice"),
    FLAMMABLE_EQUIPMENT("易燃设备", "inflammableDevice"),
    TRANSPORT_DEVICES("现场运输设备", "transportDevice");

    private final String description;
    private final String code;

    SafetyReportEnum(String description, String code) {
        this.description = description;
        this.code = code;
    }
}
