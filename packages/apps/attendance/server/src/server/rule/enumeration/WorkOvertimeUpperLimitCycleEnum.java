package com.guhecloud.cube.srv.gen.attendance.management.server.rule.enumeration;

import lombok.Getter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author bq
 * @date 2022/11/11
 * @description 加班时长上限周期
 */
@Getter
public enum WorkOvertimeUpperLimitCycleEnum {

    EVERY_DAY("每天"),

    EVERY_WEEK("每周"),

    EVERY_MONTH("每月");

    private final String value;

    WorkOvertimeUpperLimitCycleEnum(String value) {
        this.value = value;
    }

    public static List<Map<String, String>> getList() {
        List<Map<String, String>> list = new ArrayList<>();
        WorkOvertimeUpperLimitCycleEnum[] values = WorkOvertimeUpperLimitCycleEnum.values();
        for (WorkOvertimeUpperLimitCycleEnum typeEnum : values) {
            Map<String, String> map = new LinkedHashMap<>();
            map.put("name", typeEnum.getValue());
            map.put("value", typeEnum.name());
            list.add(map);
        }
        return list;
    }

}
