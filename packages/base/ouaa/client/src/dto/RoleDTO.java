package com.guhecloud.cube.biz.ouaa.client.dto;

import lombok.Data;

/**
 * @author lzy
 * @date 2021/11/25
 */
@Data
public class RoleDTO {
    /**
     * 主键
     */
    private Long id;

    /**
     * 产品主键
     */
    private Long productId;

    /**
     * 角色名称
     */
    private String name;

    /**
     * 角色图标
     */
    private String icon;

    /**
     * 排序数字
     */
    private Float sort;

    /**
     * 角色备注
     */
    private String remark;

    /**
     * 是否内置
     */
    private Boolean builtin;

    /**
     * 是否启用
     */
    private Boolean enabled;
}
