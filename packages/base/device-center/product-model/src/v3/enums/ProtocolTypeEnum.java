package com.guhecloud.cube.base.iot.product.model.v3.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 协议类型枚举
 *
 * 定义IoT设备支持的消息协议类型
 * 该枚举用于初始化系统字典表中的协议数据
 */
@Getter
@AllArgsConstructor
public enum ProtocolTypeEnum {

    /**
     * MQTT协议
     */
    MQTT("mqtt", "MQTT协议"),

    /**
     * CoAP协议
     */
    COAP("coap", "CoAP协议");
    /**
     * 协议编码
     */
    private final String code;

    /**
     * 协议描述
     */
    private final String desc;

    /**
     * 根据code获取枚举
     *
     * @param code 协议编码
     * @return 对应的枚举值,如果不存在则返回null
     */
    public static ProtocolTypeEnum fromCode(String code) {
        if (code == null) {
            return null;
        }
        for (ProtocolTypeEnum type : values()) {
            if (type.code.equalsIgnoreCase(code)) {
                return type;
            }
        }
        return null;
    }
}
