package com.guhecloud.cube.biz.mdata.server.grid.param;

import lombok.Data;

import jakarta.validation.constraints.NotNull;

/**
 * 设置网格模型全局配置参数
 */
@Data
public class GridModelConfigParam {

    /**
     * 模型项目id
     */
    @NotNull(message = "模型项目id不能为空")
    private Long modelItemId;
}

