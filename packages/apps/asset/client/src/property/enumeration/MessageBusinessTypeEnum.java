package com.guhecloud.cube.property.management.client.property.enumeration;

import lombok.Getter;

/**
 * 消息通知业务类型
 */
@Getter
public enum MessageBusinessTypeEnum {
    PROPERTY("资产管理"),
    RISK_SOURCE("风险源管理");

    private final String remarks;

    MessageBusinessTypeEnum(String remarks) {
        this.remarks = remarks;
    }
}
