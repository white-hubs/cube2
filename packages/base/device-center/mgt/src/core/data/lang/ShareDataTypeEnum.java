package com.guhecloud.cube.iot.mgt.core.data.lang;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 共享数据类型枚举
 */
public enum ShareDataTypeEnum {
    PROPERTY(1, "属性"),
    EVENT(2, "事件");

    public final int code;
    public final String name;

    ShareDataTypeEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public static ShareDataTypeEnum fromCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (ShareDataTypeEnum e : values()) {
            if (e.code == code) {
                return e;
            }
        }
        throw new IllegalArgumentException("不支持的共享数据类型: " + code);
    }

    public static List<Map<String, Object>> toList() {
        List<Map<String, Object>> list = new ArrayList<>();
        for (ShareDataTypeEnum e : values()) {
            Map<String, Object> map = new HashMap<>();
            map.put("code", e.code);
            map.put("name", e.name);
            list.add(map);
        }
        return list;
    }
}

