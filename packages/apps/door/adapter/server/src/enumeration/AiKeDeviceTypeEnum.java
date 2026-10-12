package com.guhecloud.cube.srv.gen.adapter.access.control.server.enumeration;

import lombok.Getter;
import lombok.NonNull;

@Getter
public enum AiKeDeviceTypeEnum {
    CONTROLLER("控制器"),

    FACE("人脸识别"),

    FINGER_PRINT("指纹"),

    ;

    /**
     * 描述信息
     */
    private final String desc;

    AiKeDeviceTypeEnum(@NonNull String desc) {
        this.desc = desc;
    }
}
