package com.guhecloud.cube.base.visualization.server.vo;

import lombok.Data;

@Data
public class ProjcetCanvasVO {
    /**
     * 项目信息
     */
    private ProjectManageVO projectData;
    /**
     * 实例信息
     */
    private InstanceManageVO instanceData;
    /**
     * 画布json
     */
    private String canvasJson;

}
