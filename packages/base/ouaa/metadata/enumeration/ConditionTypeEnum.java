package com.guhecloud.cube.biz.ouaa.metadata.enumeration;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/12/19 11:22
 * @Description:
 */
public enum ConditionTypeEnum {
    USER("人员"),
    DEPT("部门"),
    POST("岗位"),
    GRID_AREA("网格区域"),
    ROLE("角色");
    private final String value;

    ConditionTypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
