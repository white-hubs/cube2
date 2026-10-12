package com.guhecloud.cube.base.visualization.server.param;

import lombok.Data;

@Data
public class ProjectManageParam {
    /**
     * id
     */
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
     * 平台标题
     */
    private String title;

    /**
     * 渲染模型
     */
    private String entry;

}
