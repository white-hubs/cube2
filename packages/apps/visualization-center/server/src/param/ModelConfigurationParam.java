package com.guhecloud.cube.base.visualization.server.param;

import lombok.Data;

import jakarta.validation.constraints.NotNull;

@Data
public class ModelConfigurationParam {

    @NotNull(message = "所属项目不能为空")
    private Long projectId;
    private String modelConfiguration;
}
