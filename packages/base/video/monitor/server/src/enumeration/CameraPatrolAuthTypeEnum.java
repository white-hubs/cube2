package com.guhecloud.cube.biz.vmonitor.server.enumeration;

import lombok.Getter;

/**
 * 摄像机数据权限类型
 *
 * @author lanchun
 * @date 2023/3/22 10:14
 */
@Getter
public enum CameraPatrolAuthTypeEnum {

    DEPARTMENT("部门"),

    ROLE("角色"),

    USER("用户");

    /**
     * 描述信息
     */
    private final String description;

    CameraPatrolAuthTypeEnum(String description) {
        this.description = description;
    }
}
