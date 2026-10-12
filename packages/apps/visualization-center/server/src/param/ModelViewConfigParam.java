package com.guhecloud.cube.base.visualization.server.param;

import lombok.Data;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/7/1 18:39
 * @Description:
 */
@Data
public class ModelViewConfigParam {

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
