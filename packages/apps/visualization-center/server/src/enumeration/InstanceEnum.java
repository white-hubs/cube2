package com.guhecloud.cube.base.visualization.server.enumeration;

public enum InstanceEnum {
    DISABLE("禁用"),
    ENABLE("启用");

    private String remarks;

    InstanceEnum(String remarks) {
        this.remarks = remarks;
    }
}
