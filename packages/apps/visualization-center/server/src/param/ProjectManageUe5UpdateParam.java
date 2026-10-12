package com.guhecloud.cube.base.visualization.server.param;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/7/1 14:44
 * @Description:
 */

@Data
public class ProjectManageUe5UpdateParam {
    /**
     * id
     */
    private Long projectId;

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
