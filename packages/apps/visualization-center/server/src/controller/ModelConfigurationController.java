package com.guhecloud.cube.base.visualization.server.controller;

import com.guhecloud.cube.base.visualization.server.entity.ModelConfiguration;
import com.guhecloud.cube.base.visualization.server.param.ModelConfigurationParam;
import com.guhecloud.cube.base.visualization.server.service.ModelConfigurationService;
import com.guhecloud.cube.base.visualization.server.vo.ModelConfigurationVo;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ModelConfigurationController {

    private final ModelConfigurationService modelConfigurationService;

    /**
     * @Description 新增模型配置
     * @Author lrj
     */
    @PostMapping("/add-model-configuration")
    public void addModelConfiguration(@RequestBody ModelConfigurationParam param) {
        modelConfigurationService.addModelConfiguration(param);
    }

    /**
     * @Description 查询
     * @Author lrj
     */
    @GetMapping("/model-configuration")
    public ModelConfigurationVo getModelConfigurationPage(@RequestParam Long projectId) {
        Optional<ModelConfiguration> modelConfigurationOptional = modelConfigurationService.getModelConfiguration(projectId);
        return modelConfigurationOptional.map(modelConfiguration -> ObjectUtils.copying(modelConfiguration, ModelConfigurationVo.class))
                .orElse(null);
    }
}
