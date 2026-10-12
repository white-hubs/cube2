package com.guhecloud.cube.biz.ouaa.server.vo;

import lombok.Data;

/**
 * 角色信息
 *
 * @author Woody
 * @date 2021/7/17
 */
@Data
public class RoleVO {
    /**
     * 主键
     */
    private Long id;

    /**
     * 角色编号
     */
    private String code;

    /**
     * 角色名称
     */
    private String name;

    /**
     * 是否启用
     */
    private Boolean enabled;

    /**
     * 所属产品
     */
    private Product product;

    /**
     * 角色备注
     */
    private String remark;

    /**
     * 产品信息
     */
    @Data
    public static class Product {
        /**
         * 产品主键
         */
        private Long id;

        /**
         * 产品名称
         */
        private String name;
    }
}
