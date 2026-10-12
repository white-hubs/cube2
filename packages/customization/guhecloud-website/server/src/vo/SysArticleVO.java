package com.guhecloud.cube.customized.guhecloudwebsite.vo;

import com.guhecloud.platform.database.entity.BasicTenantEntity;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 文章VO
 */
@Data
public class SysArticleVO extends BasicTenantEntity<Long> {

    /**
     * 主键
     */
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
     * 分类名称
     */
    private String categoryName;

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
