package com.guhecloud.cube.biz.mdata.server.grid.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 网格关联类型枚举
 */
@Getter
@AllArgsConstructor
public enum GridRelationTypeEnum {
    /**
     * 部门
     */
    DEPARTMENT("DEPARTMENT", "部门"),
    /**
     * 角色
     */
    ROLE("ROLE", "角色"),
    /**
     * 人员
     */
    USER("USER", "人员"),
    /**
     * 岗位
     */
    POST("POST", "岗位"),
    /**
     * 职位
     */
    POSITION("POSITION", "职位");

    /**
     * 代码
     */
    private final String code;
    /**
     * 描述
     */
    private final String desc;
}

