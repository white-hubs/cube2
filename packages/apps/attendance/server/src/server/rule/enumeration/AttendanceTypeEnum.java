package com.guhecloud.cube.srv.gen.attendance.management.server.rule.enumeration;

import lombok.Getter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author bq
 * @date 2022/11/14
 * @description 考勤类型
 */
@Getter
public enum AttendanceTypeEnum {

    ARRANGE("排班制"),

    FIXED("固定班制");

    private final String value;

    AttendanceTypeEnum(String value) {
        this.value = value;
    }

    public static List<Map<String, String>> getList() {
        List<Map<String, String>> list = new ArrayList<>();
        AttendanceTypeEnum[] values = AttendanceTypeEnum.values();
        for (AttendanceTypeEnum typeEnum : values) {
            Map<String, String> map = new LinkedHashMap<>();
            map.put("name", typeEnum.getValue());
            map.put("value", typeEnum.name());
            list.add(map);
        }
        return list;
    }

}
