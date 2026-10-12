package com.guhecloud.cube.property.management.client.property.enumeration;

import lombok.Getter;

@Getter
public enum MessageTypeEnum {
    ADD("添加"),
    DELETED("删除");
    private final String remarks;

    MessageTypeEnum(String remarks) {
        this.remarks = remarks;
    }
}
