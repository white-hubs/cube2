package com.guhecloud.cube.srv.gen.reportmanagement.server.enumeration;

public enum StatusEnum {
    PROCESSING("处理中"),
    COMPLETED("已完成"),
    REVOKED("已撤销");
    private final String description;

    StatusEnum(String description) {
        this.description = description;
    }
}
