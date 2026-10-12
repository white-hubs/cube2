package com.guhecloud.cube.base.visualization.server.enumeration;

import lombok.Getter;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/7/7 17:03
 * @Description: 可视化文件场景
 */
@Getter
public enum VisualizationFileScene {
    VISUALIZATION_MODEL_FILE("VISUALIZATION::MODEL_FILE", "模型场景配置"),
    ;
    private final String code;
    private final String desc;

    VisualizationFileScene(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
