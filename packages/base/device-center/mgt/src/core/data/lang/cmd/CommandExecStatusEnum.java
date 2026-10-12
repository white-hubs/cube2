package com.guhecloud.cube.iot.mgt.core.data.lang.cmd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 命令执行状态枚举
 * 用于北向接口的命令响应状态码
 */
public enum CommandExecStatusEnum {
    INIT("INIT", "初始状态，尚未进行操作"),
    PROCESSING("PROCESSING", "执行中"),
    SUCCESS("SUCCESS", "成功"),
    FAIL("FAIL", "失败"),
    ERROR("ERROR", "错误"),
    REJECT("REJECT", "拒绝");

    public final String code;
    public final String desc;

    CommandExecStatusEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * 根据编码获取枚举
     *
     * @param code 状态码
     * @return 枚举对象
     */
    public static CommandExecStatusEnum fromCode(String code) {
        if (code == null) {
            return null;
        }
        for (CommandExecStatusEnum val : values()) {
            if (val.code.equalsIgnoreCase(code)) {
                return val;
            }
        }
        throw new IllegalArgumentException("不支持的命令执行状态: " + code);
    }

    /**
     * 转换为前端友好的列表
     *
     * @return Map列表
     */
    public static List<Map<String, Object>> toList() {
        List<Map<String, Object>> list = new ArrayList<>();
        for (CommandExecStatusEnum e : values()) {
            Map<String, Object> map = new HashMap<>();
            map.put("code", e.code);
            map.put("desc", e.desc);
            list.add(map);
        }
        return list;
    }
}
