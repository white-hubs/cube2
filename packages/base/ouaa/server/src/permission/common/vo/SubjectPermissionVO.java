package com.guhecloud.cube.biz.ouaa.server.permission.common.vo;

import lombok.Data;

@Data
public class SubjectPermissionVO {

    /**
     * 权限编号
     */
    private String code;

    /**
     * 权限名称
     */
    private String name;

    /**
     * 权限类型
     */
    private String type;

    /**
     * 权限配置扩充信息
     */
    private String extentConfig;
}
