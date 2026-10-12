package com.guhecloud.cube.base.iot.product.model.v3.enums;

import lombok.Getter;

@Getter
public enum DictEnum {
    PRODUCT_BRAND("PRODUCT_BRAND", 1000L),
    PRODUCT_CATEGORY("PRODUCT_CATEGORY", 1001L),
    // 系统字典
    PRODUCT_EVENT_CATEGORY("PRODUCT_EVENT_CATEGORY", 1002L),
    // 系统字典
    // 协议类型通过 {@link com.guhecloud.cube.base.iot.product.model.v3.enums.ProtocolTypeEnum} 枚举初始化
    PROTOCOL("PROTOCOL", 1003L);

    private final String code;
    private final long id;
    DictEnum(String code, long id) {
        this.code = code;
        this.id = id;
    }

}