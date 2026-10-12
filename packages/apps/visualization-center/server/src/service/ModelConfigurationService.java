package com.guhecloud.cube.base.visualization.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.base.visualization.server.entity.ModelConfiguration;
import com.guhecloud.cube.base.visualization.server.param.ModelConfigurationParam;

import java.util.Optional;

public interface ModelConfigurationService extends IService<ModelConfiguration> {
    void addModelConfiguration(ModelConfigurationParam param);

    Long copyConfiguration(Long copyProjectId, Long projectId);

    Optional<ModelConfiguration> getModelConfiguration(Long projectId);
}
