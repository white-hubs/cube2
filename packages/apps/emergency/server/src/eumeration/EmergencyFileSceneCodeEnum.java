package com.guhecloud.cube.biz.emergency.server.eumeration;

import lombok.Getter;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/1/18 16:20
 * @Description:
 */
@Getter
public enum EmergencyFileSceneCodeEnum {
    EMERGENCY_DEVICE("EMERGENCY::DEVICE", "应急设备"),
    EMERGENCY_FILE("EMERGENCY::FILE", "预案文件"),
    EMERGENCY_KNOWLEDGE("EMERGENCY::KNOWLEDGE", "应急知识"),

    ;

    private final String code;
    private final String desc;

    EmergencyFileSceneCodeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
