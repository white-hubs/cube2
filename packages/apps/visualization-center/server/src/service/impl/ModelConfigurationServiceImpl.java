package com.guhecloud.cube.base.visualization.server.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.visualization.server.dao.ModelConfigurationMapper;
import com.guhecloud.cube.base.visualization.server.entity.ModelConfiguration;
import com.guhecloud.cube.base.visualization.server.param.ModelConfigurationParam;
import com.guhecloud.cube.base.visualization.server.service.ModelConfigurationService;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.guhecloud.platform.base.util.CollectionUtils;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ModelConfigurationServiceImpl extends ServiceImpl<ModelConfigurationMapper, ModelConfiguration> implements ModelConfigurationService {

    @Override
    public void addModelConfiguration(ModelConfigurationParam param) {
        Optional<ModelConfiguration> modelConfigurationOptional = getModelConfiguration(param.getProjectId());
        if (modelConfigurationOptional.isPresent()) {
            ModelConfiguration modelConfiguration = modelConfigurationOptional.get();
            ObjectUtils.copying(param, modelConfiguration);
            this.updateById(modelConfiguration);
        } else {
            ModelConfiguration modelConfiguration = ObjectUtils.copying(param, ModelConfiguration.class);
            this.save(modelConfiguration);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long copyConfiguration(@NonNull Long copyProjectId, @NonNull Long projectId) {
        Optional<ModelConfiguration> configurationOptional = getModelConfiguration(copyProjectId);
        if (configurationOptional.isPresent()) {
            ModelConfiguration modelConfiguration = new ModelConfiguration();
            modelConfiguration.setModelConfiguration(configurationOptional.get().getModelConfiguration());
            modelConfiguration.setProjectId(projectId);
            this.baseMapper.insert(modelConfiguration);
            return modelConfiguration.getId();
        }
        return 0L;
    }

    @Override
    public Optional<ModelConfiguration> getModelConfiguration(@NonNull Long projectId) {
        List<ModelConfiguration> modelConfigurations = this.baseMapper.selectList(Wrappers.<ModelConfiguration>lambdaQuery()
                                                                                          .eq(ModelConfiguration::getProjectId, projectId));
        if (CollectionUtils.isEmpty(modelConfigurations)) {
            return Optional.empty();
        }
        return Optional.of(modelConfigurations.get(0));
    }
}
