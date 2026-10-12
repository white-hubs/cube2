package com.guhecloud.cube.base.visualization.server.param;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProjectManageUpdateParam {
    /**
     * 项目id
     */
    private Long id;
    /**
     * 缩略图
     */
    private String thumbnail;

    /**
     * 项目名称
     */
    private String projectName;

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
