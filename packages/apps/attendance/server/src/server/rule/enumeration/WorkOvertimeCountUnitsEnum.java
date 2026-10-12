package com.guhecloud.cube.srv.gen.attendance.management.server.rule.enumeration;

import lombok.Getter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author bq
 * @date 2022/11/11
 * @description 加班时长计算单位
 */
@Getter
public enum WorkOvertimeCountUnitsEnum {

    HALF_HOUR("半小时"),

    HOUR("小时"),

    DAY("天");

    private final String value;

    WorkOvertimeCountUnitsEnum(String value) {
        this.value = value;
    }

    public static List<Map<String, String>> getList() {
        List<Map<String, String>> list = new ArrayList<>();
        WorkOvertimeCountUnitsEnum[] values = WorkOvertimeCountUnitsEnum.values();
        for (WorkOvertimeCountUnitsEnum typeEnum : values) {
            Map<String, String> map = new LinkedHashMap<>();
            map.put("name", typeEnum.getValue());
            map.put("value", typeEnum.name());
            list.add(map);
        }
        return list;
    }

}
