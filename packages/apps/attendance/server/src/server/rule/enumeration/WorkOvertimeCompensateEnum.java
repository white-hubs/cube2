package com.guhecloud.cube.srv.gen.attendance.management.server.rule.enumeration;

import lombok.Getter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 加班补偿枚举
 *
 * @author bq
 * @date 2022/11/11
 */
@Getter
public enum WorkOvertimeCompensateEnum {
    LEAVE("调休"),

    FEE("计算加班费"),

    NOTHING("不做补偿");

    private final String value;

    WorkOvertimeCompensateEnum(String value) {
        this.value = value;
    }

    public static List<Map<String, String>> getList() {
        List<Map<String, String>> list = new ArrayList<>();
        WorkOvertimeCompensateEnum[] values = WorkOvertimeCompensateEnum.values();
        for (WorkOvertimeCompensateEnum typeEnum : values) {
            Map<String, String> map = new LinkedHashMap<>();
            map.put("name", typeEnum.getValue());
            map.put("value", typeEnum.name());
            list.add(map);
        }
        return list;
    }

    public static WorkOvertimeCompensateEnum getWorkOvertimeCompensateEnum(String name) {
        WorkOvertimeCompensateEnum[] values = WorkOvertimeCompensateEnum.values();
        for (WorkOvertimeCompensateEnum typeEnum : values) {
            if (name.equals(typeEnum.name())) {
                return typeEnum;
            }
        }
        return null;
    }
}
