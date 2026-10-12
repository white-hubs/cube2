package com.guhecloud.cube.base.visualization.server.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.visualization.server.dao.ProjectManageMapper;
import com.guhecloud.cube.base.visualization.server.entity.ModelConfiguration;
import com.guhecloud.cube.base.visualization.server.entity.ModelScene;
import com.guhecloud.cube.base.visualization.server.entity.ModelViewConfig;
import com.guhecloud.cube.base.visualization.server.entity.ProjectManage;
import com.guhecloud.cube.base.visualization.server.entity.RoamingRoute;
import com.guhecloud.cube.base.visualization.server.entity.RoamingRoutePoint;
import com.guhecloud.cube.base.visualization.server.param.ProjectManageParam;
import com.guhecloud.cube.base.visualization.server.param.ProjectManageUe5UpdateParam;
import com.guhecloud.cube.base.visualization.server.param.ProjectManageUpdateParam;
import com.guhecloud.cube.base.visualization.server.param.VisualizationBaseQueryParam;
import com.guhecloud.cube.base.visualization.server.service.InstanceManageService;
import com.guhecloud.cube.base.visualization.server.service.ModelConfigurationService;
import com.guhecloud.cube.base.visualization.server.service.ModelSceneService;
import com.guhecloud.cube.base.visualization.server.service.ModelViewConfigService;
import com.guhecloud.cube.base.visualization.server.service.ProjectManageService;
import com.guhecloud.cube.base.visualization.server.service.RoamingRoutePointService;
import com.guhecloud.cube.base.visualization.server.service.RoamingRouteService;
import com.guhecloud.cube.base.visualization.server.vo.ProjectManageVO;
import com.guhecloud.cube.base.visualization.server.vo.ProjectModelConfigurationVO;
import com.guhecloud.cube.base.visualization.server.vo.SmartOperationCenterConfig;
import com.guhecloud.cube.biz.mdata.client.AreaClient;
import com.guhecloud.cube.biz.mdata.client.dto.AreaDTO;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.ConvertUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 项目管理服务实现类
 *
 * @author : lijie
 * @date : 2023-8-21
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ProjectManageServiceImpl extends ServiceImpl<ProjectManageMapper, ProjectManage> implements ProjectManageService {
    private final InstanceManageService instanceManageService;
    private final ModelConfigurationService modelConfigurationService;
    private final ModelSceneService modelSceneService;
    private final ModelViewConfigService modelViewConfigService;
    private final RoamingRouteService roamingRouteService;
    private final RoamingRoutePointService roamingRoutePointService;
    private final AreaClient areaClient;

    @Override
    public ProjectManageVO queryById(Long id) {
        ProjectManage projectManage = this.baseMapper.selectById(id);
        return ConvertUtils.convert(ProjectManageVO.class, projectManage);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long copyProject(ProjectManage copyProject) {
        ProjectManage projectManage = new ProjectManage();
        projectManage.setProjectName(copyProject.getProjectName());
        projectManage.setCanvas(copyProject.getCanvas());
        projectManage.setThumbnail(copyProject.getThumbnail());
        projectManage.setModelIds(copyProject.getModelIds());
        projectManage.setMainStructure(copyProject.getMainStructure());
        this.baseMapper.insert(projectManage);
        modelConfigurationService.copyConfiguration(copyProject.getId(), projectManage.getId());
        return projectManage.getId();
    }

    @Override
    public Boolean queryByModelId(Long modelId) {
        long count = baseMapper.selectCount(new LambdaQueryWrapper<ProjectManage>()
                                                    .like(ProjectManage::getModelIds, modelId));
        return count > 0;
    }

    @Override
    public List<ProjectManageVO> queryProjectAll() {
        List<ProjectManage> projectAll = this.baseMapper.selectList(new LambdaQueryWrapper<ProjectManage>());
        return ConvertUtils.toList(ProjectManageVO.class, projectAll);
    }

    @Override
    public Boolean insert(ProjectManageParam param) {
        this.isExist(param.getProjectName());
        ProjectManage project = ConvertUtils.convert(ProjectManage.class, param);
        return baseMapper.insert(project) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean update(ProjectManageUpdateParam param) {
        ProjectManage project = baseMapper.selectById(param.getId());
        Assert.isNotFound(Objects.isNull(project), "孪生方案不存在");
        project.setProjectName(param.getProjectName());
        project.setThumbnail(param.getThumbnail());
        project.setTitle(param.getTitle());
        project.setEntry(param.getEntry());
        return baseMapper.updateById(project) > 0;
    }

    @Override
    public Boolean deleteById(Long id) {
        Boolean bool = instanceManageService.deleteByProjectId(id);
        bool = DatabaseContextHolder.softDelete(this, id);
        return bool;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateBaseConfig(ProjectManageUe5UpdateParam param) {
        ProjectManage project = baseMapper.selectById(param.getProjectId());
        Assert.isNotFound(Objects.isNull(project), "孪生方案不存在");
        project.setIconScale(param.getIconScale());
        project.setIsFilterArea(param.getIsFilterArea());
        project.setDeviceStyle(param.getDeviceStyle());
        project.setNavType(param.getNavType());
        project.setCardType(param.getCardType());
        return baseMapper.updateById(project) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void dataMigration() {
        List<ModelConfiguration> modelConfigurations = modelConfigurationService.lambdaQuery()
                .isNotNull(ModelConfiguration::getProjectId)
                .list();
        Map<String, Long> areaMap = areaClient.getAllAreaFullName()
                .stream().collect(Collectors.toMap(AreaDTO::getName, AreaDTO::getId));
        Map<Long, ProjectManage> projectManageMap = this.lambdaQuery()
                .in(ProjectManage::getId, modelConfigurations.stream().map(ModelConfiguration::getProjectId).collect(Collectors.toList()))
                .list()
                .stream().collect(Collectors.toMap(ProjectManage::getId, Function.identity()));
        List<ProjectManage> projectManageList = new ArrayList<>();
        List<ModelScene> modelSceneList = new ArrayList<>();
        List<ModelViewConfig> modelViewConfigList = new ArrayList<>();
        List<RoamingRoute> roamingRouteList = new ArrayList<>();
        List<RoamingRoutePoint> roamingRoutePointList = new ArrayList<>();
        for (ModelConfiguration modelConfiguration : modelConfigurations) {
            SmartOperationCenterConfig smartOperationCenterConfig = JSONObject.parseObject(modelConfiguration.getModelConfiguration(), SmartOperationCenterConfig.class);

            if (!projectManageMap.containsKey(modelConfiguration.getProjectId())) {
                continue;
            }
            ProjectManage projectManage = projectManageMap.get(modelConfiguration.getProjectId());
            projectManage.setTitle(smartOperationCenterConfig.getTitle());
            projectManage.setEntry(smartOperationCenterConfig.getEntry());

            if ("views".equals(smartOperationCenterConfig.getEntry())) {
                SmartOperationCenterConfig.UE5Config ue5Config = smartOperationCenterConfig.getUe5();
                projectManage.setIconScale(ue5Config.getIconScale());
                projectManage.setIsFilterArea(ue5Config.getIsFilterArea());
                projectManage.setNavType(ue5Config.getNavType());
                projectManage.setCardType(ue5Config.getCardType());
                projectManage.setDeviceStyle(ue5Config.getDeviceStyle());
                for (SmartOperationCenterConfig.ModelServer modelServer : ue5Config.getModelServer()) {
                    ModelScene modelSceneParam = new ModelScene();
                    modelSceneParam.setId(IdWorker.getId());
                    modelSceneParam.setProjectId(modelConfiguration.getProjectId());
                    modelSceneParam.setName(modelServer.getName());
                    modelSceneParam.setModelUrl(modelServer.getUrl());
                    modelSceneList.add(modelSceneParam);
                }
                for (SmartOperationCenterConfig.ModelPerspective modelPerspective : ue5Config.getModelPerspective()) {
                    ModelViewConfig modelViewConfigParam = new ModelViewConfig();
                    modelViewConfigParam.setId(IdWorker.getId());
                    modelViewConfigParam.setProjectId(modelConfiguration.getProjectId());
                    modelViewConfigParam.setLabel(modelPerspective.getLabel());
                    modelViewConfigParam.setViewValue(modelPerspective.getValue());
                    modelViewConfigParam.setAreaId(areaMap.get(modelPerspective.getArea()));
                    modelViewConfigParam.setBuildTagLocation(modelPerspective.getBuildTagLocation());
                    modelViewConfigParam.setIsDefault(modelPerspective.getIsDefault());
                    modelViewConfigList.add(modelViewConfigParam);
                }
                for (SmartOperationCenterConfig.Perspective perspective : ue5Config.getPerspective()) {
                    ModelViewConfig modelViewConfigParam = new ModelViewConfig();
                    modelViewConfigParam.setId(IdWorker.getId());
                    modelViewConfigParam.setProjectId(modelConfiguration.getProjectId());
                    modelViewConfigParam.setLabel(perspective.getLabel());
                    modelViewConfigParam.setViewValue(perspective.getCamera());
                    modelViewConfigParam.setAreaId(areaMap.get(perspective.getArea()));
                    modelViewConfigParam.setBuildTagLocation(perspective.getBuildTagLocation());
                    modelViewConfigParam.setIsDefault(perspective.getIsDefault());
                    modelViewConfigList.add(modelViewConfigParam);
                }
                for (SmartOperationCenterConfig.RoamingRoute roamingRoute : ue5Config.getRoamingRoute()) {
                    RoamingRoute roamingRouteParam = new RoamingRoute();
                    roamingRouteParam.setProjectId(modelConfiguration.getProjectId());
                    roamingRouteParam.setName(roamingRoute.getName());
                    roamingRouteParam.setId(IdWorker.getId());
                    roamingRouteList.add(roamingRouteParam);
                    for (SmartOperationCenterConfig.RoutePoint routePoint : roamingRoute.getRoute()) {
                        RoamingRoutePoint roamingRoutePointParam = new RoamingRoutePoint();
                        roamingRoutePointParam.setId(IdWorker.getId());
                        roamingRoutePointParam.setRouteId(roamingRouteParam.getId());
                        roamingRoutePointParam.setPointName(routePoint.getName());
                        roamingRoutePointParam.setPerspective(routePoint.getPerspective());
                        roamingRoutePointParam.setSort(routePoint.getSort());
                        roamingRoutePointParam.setSpeed(routePoint.getSpeed());
                        roamingRoutePointList.add(roamingRoutePointParam);
                    }
                }
            } else if ("views-three".equals(smartOperationCenterConfig.getEntry())) {
                SmartOperationCenterConfig.ThreeConfig threeConfig = smartOperationCenterConfig.getThree();
                projectManage.setIconScale(threeConfig.getIconScale());
                projectManage.setIsFilterArea(threeConfig.getIsFilterArea());
                projectManage.setNavType(threeConfig.getNavType());
                projectManage.setCardType(threeConfig.getCardType());
                projectManage.setDeviceStyle(threeConfig.getDeviceStyle());
                for (SmartOperationCenterConfig.ModelServer modelServer : threeConfig.getModelServer()) {
                    ModelScene modelSceneParam = new ModelScene();
                    modelSceneParam.setId(IdWorker.getId());
                    modelSceneParam.setProjectId(modelConfiguration.getProjectId());
                    modelSceneParam.setName(modelServer.getName());
                    modelSceneParam.setModelUrl(modelServer.getUrl());
                    modelSceneList.add(modelSceneParam);
                }
                for (SmartOperationCenterConfig.ModelPerspective modelPerspective : threeConfig.getModelPerspective()) {
                    ModelViewConfig modelViewConfigParam = new ModelViewConfig();
                    modelViewConfigParam.setId(IdWorker.getId());
                    modelViewConfigParam.setProjectId(modelConfiguration.getProjectId());
                    modelViewConfigParam.setLabel(modelPerspective.getLabel());
                    modelViewConfigParam.setViewValue(modelPerspective.getValue());
                    modelViewConfigParam.setAreaId(areaMap.get(modelPerspective.getArea()));
                    modelViewConfigParam.setBuildTagLocation(modelPerspective.getBuildTagLocation());
                    modelViewConfigParam.setIsDefault(modelPerspective.getIsDefault());
                    modelViewConfigList.add(modelViewConfigParam);
                }
                for (SmartOperationCenterConfig.Perspective perspective : threeConfig.getPerspective()) {
                    ModelViewConfig modelViewConfigParam = new ModelViewConfig();
                    modelViewConfigParam.setId(IdWorker.getId());
                    modelViewConfigParam.setProjectId(modelConfiguration.getProjectId());
                    modelViewConfigParam.setLabel(perspective.getLabel());
                    modelViewConfigParam.setViewValue(perspective.getCamera());
                    modelViewConfigParam.setAreaId(areaMap.get(perspective.getArea()));
                    modelViewConfigParam.setBuildTagLocation(perspective.getBuildTagLocation());
                    modelViewConfigParam.setIsDefault(perspective.getIsDefault());
                    modelViewConfigList.add(modelViewConfigParam);
                }
                for (SmartOperationCenterConfig.RoamingRoute roamingRoute : threeConfig.getRoamingRoute()) {
                    RoamingRoute roamingRouteParam = new RoamingRoute();
                    roamingRouteParam.setProjectId(modelConfiguration.getProjectId());
                    roamingRouteParam.setName(roamingRoute.getName());
                    roamingRouteParam.setId(IdWorker.getId());
                    roamingRouteList.add(roamingRouteParam);
                    for (SmartOperationCenterConfig.RoutePoint routePoint : roamingRoute.getRoute()) {
                        RoamingRoutePoint roamingRoutePointParam = new RoamingRoutePoint();
                        roamingRoutePointParam.setId(IdWorker.getId());
                        roamingRoutePointParam.setRouteId(roamingRouteParam.getId());
                        roamingRoutePointParam.setPointName(routePoint.getName());
                        roamingRoutePointParam.setPerspective(routePoint.getPerspective());
                        roamingRoutePointParam.setSort(routePoint.getSort());
                        roamingRoutePointParam.setSpeed(routePoint.getSpeed());
                        roamingRoutePointList.add(roamingRoutePointParam);
                    }
                }
            }
            projectManageList.add(projectManage);
        }

        this.updateBatchById(projectManageList);
        modelSceneService.saveBatch(modelSceneList);
        modelViewConfigService.saveBatch(modelViewConfigList);
        roamingRouteService.saveBatch(roamingRouteList);
        roamingRoutePointService.saveBatch(roamingRoutePointList);

    }

    @Override
    public ProjectModelConfigurationVO getModelConfigurationQueryByProjectId(Long projectId) {
        ProjectManage projectManage = this.getById(projectId);
        Assert.isNotFound(ObjectUtils.isEmpty(projectManage), "孪生项目配置不存在");
        VisualizationBaseQueryParam baseQueryParam = new VisualizationBaseQueryParam();
        baseQueryParam.setProjectId(projectId);
        return ProjectModelConfigurationVO.builder()
                .id(projectManage.getId())
                .projectName(projectManage.getProjectName())
                .title(projectManage.getTitle())
                .entry(projectManage.getEntry())
                .iconScale(projectManage.getIconScale())
                .isFilterArea(projectManage.getIsFilterArea())
                .navType(projectManage.getNavType())
                .deviceStyle(projectManage.getDeviceStyle())
                .cardType(projectManage.getCardType())
                .modelSceneList(modelSceneService.queryModelSceneByProjectId(baseQueryParam))
                .modelViewConfigList(modelViewConfigService.queryModelViewConfigByProjectId(baseQueryParam))
                .roamingRouteList(roamingRouteService.queryRoamingRouteByProjectId(baseQueryParam))
                .build();
    }

    private void isExist(String name) {
        ProjectManage projectManage = baseMapper.selectOne(new LambdaQueryWrapper<ProjectManage>()
                                                                   .eq(ProjectManage::getProjectName, name));
        if (ObjectUtils.isNotEmpty(projectManage)) {
            throw new ServiceException("名称已存在，请重新输入");
        }
    }
}
