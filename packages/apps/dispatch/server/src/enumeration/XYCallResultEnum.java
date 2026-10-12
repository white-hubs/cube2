package com.guhecloud.cube.biz.dispatch.server.enumeration;

import lombok.Getter;
import lombok.NonNull;

/**
 * 讯一呼叫结果类型枚举
 *
 * @author appet
 * @date 2022/8/1
 */
@Getter
public enum XYCallResultEnum {

    CR_1("已接"),

    CR_2("未接"),

    ;

    /**
     * 描述信息
     */
    private final String desc;

    XYCallResultEnum(@NonNull String desc) {
        this.desc = desc;
    }
}
