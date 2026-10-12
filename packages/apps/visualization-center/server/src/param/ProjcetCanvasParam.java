package com.guhecloud.cube.base.visualization.server.param;

import lombok.Data;

import jakarta.validation.constraints.NotNull;

@Data
public class ProjcetCanvasParam {
    /**
     * 缩略图
     */
    private String thumbnail;
    @NotNull(message = "json不能为空")
    private String canvasJson;
    /**
     * 项目id
     */
    @NotNull(message = "项目id不能为空")
    private Long projectId;
    /**
     * 模型id
     */
    private String modelIds;

}
