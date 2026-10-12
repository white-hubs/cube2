package com.guhecloud.cube.biz.ouaa.server.permission.common.vo;

import lombok.Data;

/**
 * 权限返回参数
 *
 * @author lanchun
 * @date 2024/5/27 10:15
 */
@Data
public class PermissionVO {

    private Long id;

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
     * 权限描述
     */
    private String description;
}
