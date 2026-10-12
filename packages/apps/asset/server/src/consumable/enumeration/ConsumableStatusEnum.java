package com.guhecloud.cube.srv.gen.property.management.server.consumable.enumeration;

import lombok.Getter;

@Getter
public enum ConsumableStatusEnum {
    DELETE("已删除"),
    NO_DELETE("未删除"),
    ;
    private final String remarks;

    ConsumableStatusEnum(String remarks) {
        this.remarks = remarks;
    }
}
