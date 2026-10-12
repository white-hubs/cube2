package com.guhecloud.cube.biz.mdata.server.bizlog.enumeration;

import lombok.Getter;

public enum OperationTypeEnum {
    LOGIN("登录"),

    ADD("新增"),

    UPDATE("修改"),

    DELETE("删除"),

    QUERY("查询"),

    EXPORT("导出"),

    IMPORT("导入");

    @Getter
    String label;

    OperationTypeEnum(String label) {
        this.label = label;
    }
}
