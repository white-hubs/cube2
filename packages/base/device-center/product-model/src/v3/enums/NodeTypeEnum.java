package com.guhecloud.cube.base.iot.product.model.v3.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 节点类型枚举
 *
 * 定义设备的节点类型
 */
@Getter
@AllArgsConstructor
public enum NodeTypeEnum {

    /**
     * 直连设备
     */
    DIRECT(1, "直连设备"),

    /**
     * 网关设备
     */
    GATEWAY(2, "网关设备"),

    /**
     * 网关子设备
     */
    GATEWAY_SUB(3, "网关子设备");

    /**
     * 类型编码
     */
    private final Integer code;

    /**
     * 类型描述
     */
    private final String desc;

    /**
     * 根据code获取枚举
     *
     * @param code 类型编码
     * @return 对应的枚举值,如果不存在则返回null
     */
    public static NodeTypeEnum fromCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (NodeTypeEnum type : values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        return null;
    }
}
