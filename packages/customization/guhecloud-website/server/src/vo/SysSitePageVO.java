package com.guhecloud.cube.customized.guhecloudwebsite.vo;

import com.guhecloud.platform.database.entity.BasicTenantEntity;
import lombok.Data;

/**
 * 页面管理VO
 */
@Data
public class SysSitePageVO extends BasicTenantEntity<Long> {

    private Long id;

    /**
     * 页面名称
     */
    private String pageName;

    /**
     * 页面标题
     */
    private String pageTitle;

    /**
     * 页面路由
     */
    private String pageRoute;

    /**
     * 绑定布局ID
     */
    private Long layoutId;

    /**
     * 导航样式 light/dark
     */
    private String metaNavStyle;

    /**
     * SEO标题
     */
    private String metaTitle;

    /**
     * SEO描述
     */
    private String metaDescription;

    /**
     * SEO关键词
     */
    private String metaKeywords;

    /**
     * 页面内容组件JSON
     */
    private String components;
    private String status;

}
