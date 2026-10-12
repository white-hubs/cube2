package com.guhecloud.cube.customized.guhecloudwebsite.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.guhecloud.platform.database.entity.BasicTenantEntity;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 文章分类
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SysArticleCategory extends BasicEnhancedTenantEntity<Long> {

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 分类名称
     */
    private String categoryName;
    private Long parentId;

    /**
     * 文章数量
     */
    private Integer articleCount;
    private Integer status;
    private Integer sort;
}
