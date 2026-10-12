package com.guhecloud.cube.base.visualization.server.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * 项目模型配置
 *
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/7/2 17:49
 * @Description:
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProjectModelConfigurationVO {
    /**
     * id
     */
    private Long id;
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

    /**
     * 模型配置
     */
    private List<ModelSceneVO> modelSceneList;

    /**
     * 模型视角配置
     */
    private List<ModelViewConfigVO> modelViewConfigList;

    /**
     * 漫游路线配置
     */
    private List<RoamingRouteVO> roamingRouteList;
}
