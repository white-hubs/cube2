package com.guhecloud.cube.iot.north.bridge.client.pj.dto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 设备状态<br/>
 * 设备真实上的状态(即通常业务上判定的设备状态)，当设备处于离线状态时，可能是由于网络原因导致的离线，或者是设备本身的故障导致的离线。<br/>
 */
public enum DeviceStatusEnum {
    NO_ACTIVE("未激活", 0),

    ON_LINE("在线", 1),

    OFF_LINE("离线", 2),

    DISABLED("已禁用", 3);

    private static final String DEVICE_STATUS_CODE = "code";

    private static final String DEVICE_STATUS_NAME = "name";

    private static final String DEVICE_STATUS_VALUE = "value";

    public final int value;
    public final String name;

    // 根据传入code获取枚举
    private static final Map<String, DeviceStatusEnum> CODE_MAP = Arrays.stream(values())
            .collect(Collectors.toMap(e -> e.name().toLowerCase(), e -> e));

    DeviceStatusEnum(String name, int value) {
        this.value = value;
        this.name = name;
    }

    public static DeviceStatusEnum valueOf(int value) {
        switch (value) {
            case 0:
                return NO_ACTIVE;
            case 1:
                return ON_LINE;
            case 2:
                return OFF_LINE;
            case 3:
                return DISABLED;
            default:
                throw new RuntimeException("非法的设备状态值:" + value);
        }
    }

    // 可选：返回一个 Map 列表，便于转换为 JSON
    public static List<Map<String, Object>> toList() {
        List<Map<String, Object>> list = new ArrayList<>();
        for (DeviceStatusEnum status : DeviceStatusEnum.values()) {
            Map<String, Object> map = new HashMap<>();
            map.put(DEVICE_STATUS_CODE, status.name().toLowerCase());
            map.put(DEVICE_STATUS_NAME, status.name);
            map.put(DEVICE_STATUS_VALUE, status.value);
            list.add(map);
        }
        return list;
    }

    /**
     * 根据字符串 code（忽略大小写）获取枚举
     */
    public static DeviceStatusEnum fromCode(String code) {
        if (code == null) {
            return null;
        }
        return CODE_MAP.get(code.toLowerCase());
    }

}
