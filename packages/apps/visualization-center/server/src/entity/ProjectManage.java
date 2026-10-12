package com.guhecloud.cube.base.visualization.server.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 项目管理;
 *
 * @author :lijie
 * @date : 2023-8-21
 */
@Data
@TableName("project_manage")
public class ProjectManage extends BasicEnhancedTenantEntity<Long> {
    /**
     * id
     */
    @TableId
    private Long id;
    /**
     * 项目名称
     */
    private String projectName;
    /**
     * 画布比例
     */
    private String canvas;
    /**
     * 主体结构
     */
    private String mainStructure;
    /**
     * 缩略图
     */
    private String thumbnail;
    private String modelIds;

    /**
     * 平台标题
     */
    private String title;

    /**
     * 渲染模型
     */
    private String entry;
    /**
     * 图片尺寸
     */
    private BigDecimal iconScale;

    /**
     * 区域设备过滤
     */
    private String isFilterArea;

    /**
     * 设备详情样式
     */
    private String deviceStyle;

    /**
     * 菜单样式
     */
    private String navType;

    /**
     * 卡片样式
     */
    private String cardType;
}
