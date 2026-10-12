package com.guhecloud.cube.srv.gen.maintain.server.electron.enumeration;

import lombok.Getter;

@Getter
public enum MessageUserTypeEnum {
    DEPARTMENT("部门"),

    ROLE("角色"),

    POST("岗位"),

    USER("用户");
    private String remarks;

    MessageUserTypeEnum(String remarks) {
        this.remarks = remarks;
    }
}
