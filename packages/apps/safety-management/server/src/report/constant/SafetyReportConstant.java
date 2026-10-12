package com.guhecloud.cube.srv.gen.safety.management.report.constant;

import com.guhecloud.cube.srv.gen.safety.management.report.enumeration.SafetyReportEnum;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.guhecloud.cube.srv.gen.safety.management.report.enumeration.SafetyReportEnum.CURRENT;
import static com.guhecloud.cube.srv.gen.safety.management.report.enumeration.SafetyReportEnum.ELECTRIC_VEHICLES_ELEVATOR;
import static com.guhecloud.cube.srv.gen.safety.management.report.enumeration.SafetyReportEnum.FIRE_FIGHTING_EQUIPMENT;
import static com.guhecloud.cube.srv.gen.safety.management.report.enumeration.SafetyReportEnum.FLAMMABLE_EQUIPMENT;
import static com.guhecloud.cube.srv.gen.safety.management.report.enumeration.SafetyReportEnum.GAS_LEAKAGE;
import static com.guhecloud.cube.srv.gen.safety.management.report.enumeration.SafetyReportEnum.HAND_REPORT;
import static com.guhecloud.cube.srv.gen.safety.management.report.enumeration.SafetyReportEnum.HUMIDITY;
import static com.guhecloud.cube.srv.gen.safety.management.report.enumeration.SafetyReportEnum.HYDRAULIC_PRESSURE;
import static com.guhecloud.cube.srv.gen.safety.management.report.enumeration.SafetyReportEnum.OCCUPY_OPERATIONS;
import static com.guhecloud.cube.srv.gen.safety.management.report.enumeration.SafetyReportEnum.OPEN_FIRE;
import static com.guhecloud.cube.srv.gen.safety.management.report.enumeration.SafetyReportEnum.PARKING_VIOLATION;
import static com.guhecloud.cube.srv.gen.safety.management.report.enumeration.SafetyReportEnum.SMOKE;
import static com.guhecloud.cube.srv.gen.safety.management.report.enumeration.SafetyReportEnum.TEMPERATURE;
import static com.guhecloud.cube.srv.gen.safety.management.report.enumeration.SafetyReportEnum.TRAFFIC_JAM;
import static com.guhecloud.cube.srv.gen.safety.management.report.enumeration.SafetyReportEnum.TRANSPORT_DEVICES;
import static com.guhecloud.cube.srv.gen.safety.management.report.enumeration.SafetyReportEnum.UPS;
import static com.guhecloud.cube.srv.gen.safety.management.report.enumeration.SafetyReportEnum.VIOLATION_FIRE;
import static com.guhecloud.cube.srv.gen.safety.management.report.enumeration.SafetyReportEnum.VOLTAGE;
import static com.guhecloud.cube.srv.gen.safety.management.report.enumeration.SafetyReportEnum.WARM_SENSATION;

public class SafetyReportConstant {

    /**
     * 应用名称
     */
    public static final String FIRE_CONTROL = "FIRECONTROL";
    public static final String ROTATING_MANAGE = "ROTATINGMANAGE";
    public static final String ELECTRIC_MANAGE = "ELECTRICMANAGE";
    public static final String SECURITY_MONITORING = "SECURITY_MONITORING";
    public static final String GAS_MANAGEMENT = "GAS_MANAGEMENT";
    /**
     * 报警等级
     */
    public static final String ONE_LEVEL = "one_level";
    public static final String TWO_LEVEL = "two_level";
    /**
     * 数据分组模式
     */
    public static final String LEVEL = "LEVEL";
    public static final String APPLICATION = "APPLICATION";

    /**
     * 报警启用情况消息模板
     *
     * @return
     */
    public static final String NORMAL_MESSAGE = "针对%s事件，都正常建立了%s机制且都在正常运行中;";

    public static final String UN_ENABLE_MESSAGE = "针对%s事件，存在异常情况，%s机制处于关闭状态;";

    public static final String ERROR_MESSAGE = "针对%s事件，存在异常情况，没有建立%s机制;";
    public static final String RISK_ERROR_MESSAGE = "%s还未建档，请知悉。";
    public static final String ALARM_RESPONSE_SORT = "%s响应率最佳，响应率分别在%s。各部门处理排名如下：";
    public static final String ALARM_RESPONSE_SORT_USER = "%s响应率最佳，响应率分别在%s。";
    public static final String ALARM_HANDLER_SORT = "%s处理率最佳，处理率分别在%s；";
    public static final String ALARM_OUT_OF_DAY_SORT = "%s处理效率最佳，跨天处理率分别在%s。各部门处理排名如下：";
    public static final String ALARM_OUT_OF_DAY_SORT_USER = "%s处理效率最佳，跨天处理率分别在%s";
    public static final String NO_DATA_TEMP = "暂无数据";

    public static List<String> getApplicationCodeList() {
        return Stream.of(FIRE_CONTROL, ELECTRIC_MANAGE, ROTATING_MANAGE, SECURITY_MONITORING, GAS_MANAGEMENT)
                .collect(Collectors.toList());
    }

    public static List<String> getAlarmEventList() {
        return Stream.of(CURRENT.getDescription(), VOLTAGE.getDescription(), TEMPERATURE.getDescription(),
                         OPEN_FIRE.getDescription(), SMOKE.getDescription(), VIOLATION_FIRE.getDescription(),
                         HYDRAULIC_PRESSURE.getDescription(), HAND_REPORT.getDescription(), WARM_SENSATION.getDescription(),
                         GAS_LEAKAGE.getDescription(), PARKING_VIOLATION.getDescription(), TRAFFIC_JAM.getDescription(),
                         OCCUPY_OPERATIONS.getDescription(), ELECTRIC_VEHICLES_ELEVATOR.getDescription(),
                         HUMIDITY.getDescription(), UPS.getDescription()
                        )
                .collect(Collectors.toList());
    }

    /**
     * 消防
     *
     * @return
     */
    public static List<SafetyReportEnum> getFireAlarmEventList() {
        return Stream.of(OPEN_FIRE, SMOKE, VIOLATION_FIRE,
                         HYDRAULIC_PRESSURE, HAND_REPORT, WARM_SENSATION
                        )
                .collect(Collectors.toList());
    }

    /**
     * 电气
     *
     * @return
     */
    public static List<SafetyReportEnum> getElectricAlarmEventList() {
        return Stream.of(CURRENT, VOLTAGE, TEMPERATURE)
                .collect(Collectors.toList());
    }

    /**
     * 动环监控
     *
     * @return
     */
    public static List<SafetyReportEnum> getRoatingAlarmEventList() {
        return Stream.of(HUMIDITY, UPS)
                .collect(Collectors.toList());
    }

    /**
     * 安防监控
     *
     * @return
     */
    public static List<SafetyReportEnum> getSecurityAlarmEventList() {
        return Stream.of(PARKING_VIOLATION, TRAFFIC_JAM,
                         OCCUPY_OPERATIONS, ELECTRIC_VEHICLES_ELEVATOR)
                .collect(Collectors.toList());
    }

    public static List<SafetyReportEnum> getGasAlarmEventList() {
        return Stream.of(GAS_LEAKAGE)
                .collect(Collectors.toList());
    }

    public static List<String> getDeviceNameList() {
        return Stream.of(FIRE_FIGHTING_EQUIPMENT.name(), FLAMMABLE_EQUIPMENT.name(), TRANSPORT_DEVICES.name())
                .collect(Collectors.toList());
    }

    public static List<SafetyReportEnum> getDeviceNameEnumList() {
        return Stream.of(FIRE_FIGHTING_EQUIPMENT, FLAMMABLE_EQUIPMENT, TRANSPORT_DEVICES)
                .collect(Collectors.toList());
    }
}
