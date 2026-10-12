package com.guhecloud.cube.srv.gen.property.management.server.property.enumeration;

import lombok.Getter;

/**
 * @Author: lijie
 * @CreateTime: 2024-04-07  16:30
 * @Description:
 * @Version: 1.0
 */
@Getter
public enum PropertyBimConfigEnum {
    ALL("所有");
    private final String remarks;

    PropertyBimConfigEnum(String remarks) {
        this.remarks = remarks;
    }
}
