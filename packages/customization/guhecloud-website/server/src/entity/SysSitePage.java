package com.guhecloud.cube.customized.guhecloudwebsite.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 页面管理实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysSitePage extends BasicEnhancedTenantEntity<Long> {

    @TableId(type = IdType.ASSIGN_ID)
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
