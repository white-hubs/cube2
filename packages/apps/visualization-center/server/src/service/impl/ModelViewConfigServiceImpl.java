package com.guhecloud.cube.base.visualization.server.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.visualization.server.dao.ModelViewConfigMapper;
import com.guhecloud.cube.base.visualization.server.entity.ModelViewConfig;
import com.guhecloud.cube.base.visualization.server.param.ModelViewConfigParam;
import com.guhecloud.cube.base.visualization.server.param.VisualizationBaseQueryParam;
import com.guhecloud.cube.base.visualization.server.service.ModelViewConfigService;
import com.guhecloud.cube.base.visualization.server.vo.ModelViewConfigVO;
import com.guhecloud.cube.biz.mdata.client.AreaClient;
import com.guhecloud.cube.biz.mdata.client.dto.AreaDTO;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/7/1 18:35
 * @Description:
 */
@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ModelViewConfigServiceImpl extends ServiceImpl<ModelViewConfigMapper, ModelViewConfig> implements ModelViewConfigService {
    private final AreaClient areaClient;

    @Override
    public List<ModelViewConfigVO> queryModelViewConfigByProjectId(VisualizationBaseQueryParam param) {
        Assert.isParameterError(Objects.isNull(param.getProjectId()), "项目id不能为空");
        List<ModelViewConfig> modelViewConfigs = this.lambdaQuery()
                .eq(StringUtils.isNotBlank(param.getKeyword()), ModelViewConfig::getLabel, param.getKeyword())
                .eq(Objects.nonNull(param.getAreaId()), ModelViewConfig::getAreaId, param.getAreaId())
                .eq(ModelViewConfig::getProjectId, param.getProjectId())
                .list();
        if (CollectionUtils.isEmpty(modelViewConfigs)) {
            return new ArrayList<>();
        }

        Map<Long, AreaDTO> areaMap = Optional.ofNullable(areaClient.listAreas())
                .orElse(new ArrayList<>())
                .stream().collect(Collectors.toMap(AreaDTO::getId, Function.identity()));
        return modelViewConfigs.stream().map(t -> {
                                                 ModelViewConfigVO modelViewConfigVO = new ModelViewConfigVO();
                                                 modelViewConfigVO.setProjectId(t.getProjectId());
                                                 modelViewConfigVO.setId(t.getId());
                                                 modelViewConfigVO.setLabel(t.getLabel());
                                                 modelViewConfigVO.setViewValue(t.getViewValue());
                                                 modelViewConfigVO.setAreaId(t.getAreaId());

                                                 AreaDTO areaDTO = areaMap.get(t.getAreaId());
                                                 if (Objects.nonNull(areaDTO)) {
                                                     String areaName = Arrays.stream(areaDTO.getSequence().split("-"))
                                                             .filter(area -> areaMap.containsKey(Long.valueOf(area)))
                                                             .map(area -> areaMap.get(Long.valueOf(area)).getName())
                                                             .collect(Collectors.joining("-"));
                                                     modelViewConfigVO.setAreaName(areaName);
                                                 }
                                                 modelViewConfigVO.setBuildTagLocation(t.getBuildTagLocation());
                                                 modelViewConfigVO.setHideActors(t.getHideActors());
                                                 modelViewConfigVO.setIsDefault(t.getIsDefault());
                                                 return modelViewConfigVO;
                                             }
                                            ).collect(Collectors.toList());
    }

    @Override
    public Boolean saveOrUpdateModelViewConfig(ModelViewConfigParam modelViewConfigParam) {
        if (Objects.nonNull(modelViewConfigParam.getId())) {
            ModelViewConfig modelViewConfig = this.getById(modelViewConfigParam.getId());
            Assert.isNotFound(Objects.isNull(modelViewConfig), "模型场景配置不存在");
            convertModelViewConfig(modelViewConfigParam, modelViewConfig);
            return this.updateById(modelViewConfig);
        } else {
            ModelViewConfig modelViewConfig = convertModelViewConfig(modelViewConfigParam, null);
            modelViewConfig.setId(IdWorker.getId());
            return this.save(modelViewConfig);

        }
    }

    private ModelViewConfig convertModelViewConfig(ModelViewConfigParam param, ModelViewConfig modelViewConfig) {
        modelViewConfig = Objects.nonNull(modelViewConfig) ? modelViewConfig : new ModelViewConfig();
        modelViewConfig.setLabel(param.getLabel());
        modelViewConfig.setProjectId(param.getProjectId());
        modelViewConfig.setViewValue(param.getViewValue());
        modelViewConfig.setAreaId(param.getAreaId());
        modelViewConfig.setBuildTagLocation(param.getBuildTagLocation());
        modelViewConfig.setHideActors(param.getHideActors());
        modelViewConfig.setIsDefault(param.getIsDefault());
        return modelViewConfig;
    }
}
