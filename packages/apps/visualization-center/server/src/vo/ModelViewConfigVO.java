package com.guhecloud.cube.base.visualization.server.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/7/1 18:49
 * @Description:
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ModelViewConfigVO {

    private Long id;

    /**
     * 项目id
     */
    private Long projectId;

    /**
     * 视角名称
     */
    private String label;

    /**
     * 视角参数
     */
    private String viewValue;

    /**
     * 区域id
     */
    private Long areaId;

    /**
     * 区域名称
     */
    private String areaName;

    /**
     * 中心点位置
     */
    private String buildTagLocation;

    /**
     * 是否默认
     */
    private String isDefault;

    /**
     * 隐藏区域
     */
    private String hideActors;
}
