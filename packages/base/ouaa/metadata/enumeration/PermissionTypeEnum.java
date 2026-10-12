package com.guhecloud.cube.biz.ouaa.metadata.enumeration;

public enum PermissionTypeEnum {
    /**
     * 功能、数据
     */
    FUNCATION("FUNCATION"),
    DATA("DATA");
    private final String value;

    PermissionTypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
