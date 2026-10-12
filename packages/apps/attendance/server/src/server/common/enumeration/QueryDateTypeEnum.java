package com.guhecloud.cube.srv.gen.attendance.management.server.common.enumeration;

import lombok.Getter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @description 查询条件日期类型
 */
@Getter
public enum QueryDateTypeEnum {

    MONTH("选择固定的月份"),

    THREE("近三月"),

    SIX("近六月"),

    YEAR("选择固定的年"),

    ;

    private final String value;

    QueryDateTypeEnum(String value) {
        this.value = value;
    }

    public static List<Map<String, String>> getList() {
        List<Map<String, String>> list = new ArrayList<>();
        QueryDateTypeEnum[] values = QueryDateTypeEnum.values();
        for (QueryDateTypeEnum typeEnum : values) {
            Map<String, String> map = new LinkedHashMap<>();
            map.put("name", typeEnum.getValue());
            map.put("value", typeEnum.name());
            list.add(map);
        }
        return list;
    }

}
