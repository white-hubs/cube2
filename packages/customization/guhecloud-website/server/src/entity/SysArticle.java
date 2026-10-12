package com.guhecloud.cube.customized.guhecloudwebsite.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.guhecloud.platform.database.entity.BasicTenantEntity;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 文章
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SysArticle extends BasicEnhancedTenantEntity<Long> {

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 文章标题
     */
    private String title;

    /**
     * 分类ID
     */
    private Long categoryId;

    /**
     * 文章封面
     */
    private String coverImageUrl;

    /**
     * 内容简介
     */
    private String summary;

    /**
     * 文章内容
     */
    private String content;

    /**
     * 作者
     */
    private String author;

    /**
     * 状态 1显示 0不显示
     */
    private Integer status;

    /**
     * 浏览量
     */
    private Integer viewCount;

    /** 发布日期 */
    private String date;

    /** 排序 */
    private Integer sort;

}
