package com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.domain.valueobject;

import lombok.Getter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 排班方式枚举
 */
@Getter
public enum AttSchedulingWaysEnum {

    SHIFTS("倒班制"),

    FIXED("固定班制");

    private final String value;

    AttSchedulingWaysEnum(String value) {
        this.value = value;
    }

    public static List<Map<String, String>> getList() {
        List<Map<String, String>> list = new ArrayList<>();
        AttSchedulingWaysEnum[] values = AttSchedulingWaysEnum.values();
        for (AttSchedulingWaysEnum typeEnum : values) {
            Map<String, String> map = new LinkedHashMap<>();
            map.put("name", typeEnum.getValue());
            map.put("value", typeEnum.name());
            list.add(map);
        }
        return list;
    }

}
