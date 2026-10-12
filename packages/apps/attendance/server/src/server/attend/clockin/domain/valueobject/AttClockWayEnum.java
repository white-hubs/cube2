package com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.domain.valueobject;

import lombok.Getter;

@Getter
public enum AttClockWayEnum {
    /**
     * 设备打卡
     */
    DEVICE("考勤机打卡"),

    /**
     * 地点打卡
     */
    GPS("GPS打卡");
    private final String value;

    AttClockWayEnum(String value) {
        this.value = value;
    }

    public static String getValueByName(String name) {
        if (null == name) {
            return "";
        }
        AttClockWayEnum[] wayEnum = AttClockWayEnum.values();
        String value = "";
        for (AttClockWayEnum ex : wayEnum) {
            if (name.equals(ex.name())) {
                value = ex.getValue();
                break;
            }
        }
        return value;
    }
}
