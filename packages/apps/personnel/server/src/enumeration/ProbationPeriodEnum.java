package com.guhecloud.cube.srv.gen.personnel.management.server.enumeration;

import lombok.Getter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author bq
 * @date 2022/11/8
 * @description 试用期
 */
@Getter
public enum ProbationPeriodEnum {

    ONE_MONTH("1个月"),
    TWO_MONTH("2个月"),
    THREE_MONTH("3个月"),
    SIX_MONTH("6个月");

    String value;

    ProbationPeriodEnum(String value) {
        this.value = value;
    }

    public static String getNameByValues(String value) {
        String name = "";
        ProbationPeriodEnum[] values = ProbationPeriodEnum.values();
        for (ProbationPeriodEnum typeEnum : values) {
            if (typeEnum.value.equals(value)) {
                name = typeEnum.name();
            }
        }
        return name;
    }

    public static List<Map<String, String>> getList() {
        List<Map<String, String>> list = new ArrayList<>();
        ProbationPeriodEnum[] values = ProbationPeriodEnum.values();
        for (ProbationPeriodEnum typeEnum : values) {
            Map<String, String> map = new LinkedHashMap<>();
            map.put("name", typeEnum.getValue());
            map.put("value", typeEnum.name());
            list.add(map);
        }
        return list;
    }

}
