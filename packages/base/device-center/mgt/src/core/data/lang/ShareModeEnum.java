package com.guhecloud.cube.iot.mgt.core.data.lang;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 共享模式枚举
 */
public enum ShareModeEnum {
    SHARE(1, "共享", "源设备和目标设备都接收数据"),
    TRANSFER(2, "转换", "仅目标设备接收数据");

    public final int code;
    public final String name;
    public final String desc;

    ShareModeEnum(int code, String name, String desc) {
        this.code = code;
        this.name = name;
        this.desc = desc;
    }

    public static ShareModeEnum fromCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (ShareModeEnum e : values()) {
            if (e.code == code) {
                return e;
            }
        }
        throw new IllegalArgumentException("不支持的共享模式: " + code);
    }

    public static List<Map<String, Object>> toList() {
        List<Map<String, Object>> list = new ArrayList<>();
        for (ShareModeEnum e : values()) {
            Map<String, Object> map = new HashMap<>();
            map.put("code", e.code);
            map.put("name", e.name);
            map.put("desc", e.desc);
            list.add(map);
        }
        return list;
    }
}

