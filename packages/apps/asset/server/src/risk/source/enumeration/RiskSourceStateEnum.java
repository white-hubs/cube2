package com.guhecloud.cube.srv.gen.property.management.server.risk.source.enumeration;

import lombok.Getter;

/**
 * @Author: lijie
 * @CreateTime: 2024-02-01  10:20
 * @Description:
 * @Version: 1.0
 */
@Getter
public enum RiskSourceStateEnum {
    EXPIRE("已过期"),
    NORMAL("未过期");
    private final String remarks;

    RiskSourceStateEnum(String remarks) {
        this.remarks = remarks;
    }
}
