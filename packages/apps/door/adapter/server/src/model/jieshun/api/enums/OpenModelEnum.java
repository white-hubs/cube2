package com.guhecloud.cube.srv.gen.adapter.access.control.server.model.jieshun.api.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum OpenModelEnum {
    /**
     * 开门：1，关门：2
     * 常开：3，取消常开：8
     */
    OPEN(1, "开门"),
    CLOSE(2, "关门"),
    CONSTANT_OPEN(3, "常开"),
    CANCEL_CONSTANT_OPEN(8, "取消常开");

    private final Integer code;
    private final String desc;

    public static OpenModelEnum getBy(ControTypeEnum controTypeEnum) {
        if (controTypeEnum == ControTypeEnum.OPEN) {
            return OPEN;
        }
        if (controTypeEnum == ControTypeEnum.CLOSE) {
            return CLOSE;
        }
        if (controTypeEnum == ControTypeEnum.CONSTANT_OPEN) {
            return CONSTANT_OPEN;
        }
        if (controTypeEnum == ControTypeEnum.CANCEL_CONSTANT_OPEN) {
            return CANCEL_CONSTANT_OPEN;
        }
        throw new RuntimeException("不支持的开启模式枚举类型");
    }
}
