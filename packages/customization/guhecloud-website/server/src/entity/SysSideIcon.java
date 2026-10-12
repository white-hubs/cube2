package com.guhecloud.cube.customized.guhecloudwebsite.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.guhecloud.platform.database.entity.BasicTenantEntity;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 侧边图标实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysSideIcon extends BasicEnhancedTenantEntity<Long> {

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 图标
     */
    private String icon;

    /**
     * 概述
     */
    private String summary;

    /**
     * 图片
     */
    private String image;

    /**
     * 跳转链接
     */
    private String link;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 状态：1显示，0隐藏
     */
    private Integer status;

    /**
     * 是否开启：1开启，0关闭
     */
    private Integer isOpen;

}
