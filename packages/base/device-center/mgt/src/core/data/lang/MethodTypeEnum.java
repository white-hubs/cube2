package com.guhecloud.cube.iot.mgt.core.data.lang;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 方法类型枚举
 * 用于产品方法的调用类型
 */
public enum MethodTypeEnum {
    SYNC("sync", "同步方法"),
    ASYNC("async", "异步方法");

    public final String code;
    public final String desc;

    MethodTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * 根据编码获取枚举
     *
     * @param code 方法类型编码
     * @return 枚举对象
     */
    public static MethodTypeEnum fromCode(String code) {
        if (code == null) {
            return null;
        }
        for (MethodTypeEnum val : values()) {
            if (val.code.equalsIgnoreCase(code)) {
                return val;
            }
        }
        throw new IllegalArgumentException("不支持的方法类型: " + code);
    }

    /**
     * 转换为前端友好的列表
     *
     * @return Map列表
     */
    public static List<Map<String, Object>> toList() {
        List<Map<String, Object>> list = new ArrayList<>();
        for (MethodTypeEnum e : values()) {
            Map<String, Object> map = new HashMap<>();
            map.put("code", e.code);
            map.put("desc", e.desc);
            list.add(map);
        }
        return list;
    }
}
