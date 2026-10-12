package com.guhecloud.cube.srv.gen.personnel.management.server.enumeration;

import lombok.Getter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 婚姻状况枚举
 */
@Getter
public enum MaritalStatusEnum {
    UNMARRIED("未婚"),
    MARRIED("已婚"),
    DIVORCED("离异"),
    WIDOWED("丧偶");

    private final String value;

    MaritalStatusEnum(String value) {
        this.value = value;
    }

    public static List<Map<String, String>> getList() {
        List<Map<String, String>> list = new ArrayList<>();
        for (MaritalStatusEnum maritalStatus : MaritalStatusEnum.values()) {
            Map<String, String> map = new LinkedHashMap<>();
            map.put("name", maritalStatus.getValue());
            map.put("value", maritalStatus.name());
            list.add(map);
        }
        return list;
    }
}
