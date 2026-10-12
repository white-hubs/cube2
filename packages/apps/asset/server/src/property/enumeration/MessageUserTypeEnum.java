package com.guhecloud.cube.srv.gen.property.management.server.property.enumeration;

import lombok.Getter;

@Getter
public enum MessageUserTypeEnum {
    DEPARTMENT("部门"),

    ROLE("角色"),

    POST("岗位"),

    USER("用户");
    private final String remarks;

    MessageUserTypeEnum(String remarks) {
        this.remarks = remarks;
    }
}
