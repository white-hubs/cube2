package com.guhecloud.cube.srv.gen.property.management.server.common.enumeration;

import lombok.Getter;

@Getter
public enum NotifyTypeEnum {
    DEPARTMENT("部门"),

    ROLE("角色"),

    POST("岗位"),

    USER("用户");

    private final String remarks;

    NotifyTypeEnum(String remarks) {
        this.remarks = remarks;
    }
}
