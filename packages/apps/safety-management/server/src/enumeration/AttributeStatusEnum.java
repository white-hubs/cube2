package com.guhecloud.cube.srv.gen.safety.management.enumeration;

import lombok.Getter;

/**
 * @author wql
 * @describe 设备状态枚举
 * @date 2023/9/12 20:22
 */
@Getter
public enum AttributeStatusEnum {
    OFFLINE("离线"),

    ONLINE("在线"),
    ;

    final String name;

    AttributeStatusEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
