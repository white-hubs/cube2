package com.guhecloud.platform.security.permission.model;

import lombok.Data;

import java.util.Set;

@Data
public class Permission {

    private Long id;

    /**
     * 权限编号
     */
    private String code;

    /**
     * 权限类型
     */
    private String type;

    /**
     * 接口编号
     */
    private Set<String> interfaceCodes;

    /**
     * 权限扩展配置
     */
    private String extentConfig;
}
