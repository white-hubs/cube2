package com.guhecloud.cube.srv.gen.adapter.access.control.server.model.jieshun.api.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 出入口类型枚举
 * 对应数据字典值
 */
@AllArgsConstructor
@Getter
public enum DeviceIoTypeEnum {
    ENTRANCE(1, "入口"),
    EXIT(2, "出口"),
    BIDIRECTIONAL(3, "双向通道"),
    EMERGENCY(4, "紧急出口"),
    // 根据实际数据字典补充...
    UNKNOWN(99, "未知类型");
    private final Integer code;
    private final String desc;
}