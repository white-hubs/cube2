package com.guhecloud.cube.srv.gen.personnel.management.server.enumeration;

import lombok.Getter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 性别枚举
 *
 * @author bq
 * @date 2022/11/09
 */
@Getter
public enum GenderEnum {
    MALE("男"),
    FEMALE("女"),
    NONE("保密");

    String value;

    GenderEnum(String value) {
        this.value = value;
    }

    public static String getNameByValues(String value) {
        String name = "";
        GenderEnum[] values = GenderEnum.values();
        for (GenderEnum typeEnum : values) {
            if (typeEnum.value.equals(value)) {
                name = typeEnum.name();
            }
        }
        return name;
    }

    public static List<Map<String, String>> getList() {
        List<Map<String, String>> list = new ArrayList<>();
        GenderEnum[] values = GenderEnum.values();
        for (GenderEnum typeEnum : values) {
            Map<String, String> map = new LinkedHashMap<>();
            map.put("name", typeEnum.getValue());
            map.put("value", typeEnum.name());
            list.add(map);
        }
        return list;
    }

}
