package com.guhecloud.cube.biz.mdata.server.scene.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.base.file.client.FileClient;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.biz.mdata.server.area.entity.Area;
import com.guhecloud.cube.biz.mdata.server.area.service.AreaService;
import com.guhecloud.cube.biz.mdata.server.area.vo.AreaVO;
import com.guhecloud.cube.biz.mdata.server.scene.entity.AreaSceneConfig;
import com.guhecloud.cube.biz.mdata.server.scene.entity.BusinessPanelConfig;
import com.guhecloud.cube.biz.mdata.server.scene.entity.SceneType;
import com.guhecloud.cube.biz.mdata.server.scene.entity.UniversalPanel;
import com.guhecloud.cube.biz.mdata.server.scene.enumeration.BusinessPanelConfigTypeEnum;
import com.guhecloud.cube.biz.mdata.server.scene.param.AreaSceneConfigModifyParam;
import com.guhecloud.cube.biz.mdata.server.scene.param.AreaScenePanelConfigBatchModifyParam;
import com.guhecloud.cube.biz.mdata.server.scene.param.BusinessPanelConfigModifyParam;
import com.guhecloud.cube.biz.mdata.server.scene.service.AreaSceneConfigService;
import com.guhecloud.cube.biz.mdata.server.scene.service.BusinessPanelConfigService;
import com.guhecloud.cube.biz.mdata.server.scene.service.SceneTypeService;
import com.guhecloud.cube.biz.mdata.server.scene.service.UniversalPanelService;
import com.guhecloud.cube.biz.mdata.server.scene.vo.AreaSceneConfigVO;
import com.guhecloud.cube.biz.mdata.server.scene.vo.BusinessPanelConfigVO;
import com.guhecloud.cube.biz.mdata.server.scene.vo.SceneTypeBimVO;
import com.guhecloud.cube.biz.mdata.server.scene.vo.SceneTypeVO;
import com.guhecloud.cube.biz.mdata.server.scene.vo.UniversalPanelVo;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 区域场景配置接口
 *
 * @author lanchun
 * @date 2021/12/2 15:04
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AreaSceneConfigController {

    private final AreaSceneConfigService areaSceneConfigService;

    private final SceneTypeService sceneTypeService;

    private final AreaService areaService;

    private final FileClient fileClient;

    private final BusinessPanelConfigService businessPanelConfigService;

    private final UniversalPanelService universalPanelService;

    /**
     * 区域场景配置新增
     *
     * @param param 参数
     *
     * @return
     */
    @PostMapping("/area-scene-config")
    public Long save(@Valid @RequestBody AreaSceneConfigModifyParam param) {
        SceneType sceneType = sceneTypeService.getById(param.getSceneType());
        param.setProjectId(sceneType.getProjectId());
        return this.areaSceneConfigService.save(param);
    }

    /**
     * 区域场景面板配置新增
     *
     * @param param
     *
     * @return
     */
    @PostMapping("/area-scene-config/panel/disposable-save")
    public void disposableSave(@Valid @RequestBody AreaScenePanelConfigBatchModifyParam param) {
        this.areaSceneConfigService.disposableSave(param);
    }

    /**
     * 区域场景配置查询
     *
     * @param areaId    区域主键
     * @param sceneType 场景类型
     *
     * @return
     */
    @GetMapping("/area-scene-config/{areaId}/{sceneType}")
    public AreaSceneConfigVO getAreaScene(@PathVariable("areaId") Long areaId, @PathVariable("sceneType") Long sceneType) {
        AreaSceneConfig areaSceneConfig = this.areaSceneConfigService.getByAreaScene(areaId, sceneType);
        if (Objects.isNull(areaSceneConfig)) {
            return null;
        }
        return ObjectUtils.copying(areaSceneConfig, AreaSceneConfigVO.class);
    }

    /**
     * 区域场景面板配置
     *
     * @param param
     *
     * @return
     */
    @PostMapping("/area-scene-config/panel")
    public void AreaScenePanelSave(@Valid @RequestBody AreaScenePanelConfigBatchModifyParam param) {
        AreaSceneConfig areaSceneConfig = this.areaSceneConfigService.getByAreaScene(param.getAreaId(), param.getSceneType());
        Assert.isNotFound(areaSceneConfig, "不存在的场景配置");
        List<BusinessPanelConfigModifyParam> panelConfigModifyParams = ObjectUtils.copying(param.getPanelConfigModifyParams(), BusinessPanelConfigModifyParam.class);
        panelConfigModifyParams.stream().forEach(panelConfigModifyParam -> {
            panelConfigModifyParam.setBusinessId(areaSceneConfig.getId());
            panelConfigModifyParam.setBusinessType(BusinessPanelConfigTypeEnum.AREA_SCENE);
        });
        this.businessPanelConfigService.saveBatch(panelConfigModifyParams);
    }

    /**
     * 区域场景面板配置列表
     *
     * @param areaId
     * @param sceneType
     *
     * @return
     */
    @GetMapping("/area-scene-config/panel/{areaId}/{sceneType}")
    public List<BusinessPanelConfigVO> listAreaScenePanels(@PathVariable("areaId") Long areaId, @PathVariable("sceneType") Long sceneType) {
        AreaSceneConfig areaSceneConfig = this.areaSceneConfigService.getByAreaScene(areaId, sceneType);
        if (Objects.isNull(areaSceneConfig)) {
            return Collections.emptyList();
        }
        List<BusinessPanelConfig> list = this.businessPanelConfigService.listByBusinessId(areaSceneConfig.getProjectId(), areaSceneConfig.getId());
        List<BusinessPanelConfigVO> businessPanelConfigVOS = ObjectUtils.copying(list, BusinessPanelConfigVO.class);
        for (BusinessPanelConfigVO businessPanelConfigVO : businessPanelConfigVOS) {
            UniversalPanel one = universalPanelService.getOne(Wrappers.<UniversalPanel>lambdaQuery()
                                                                      .eq(UniversalPanel::getBusinessId, businessPanelConfigVO.getId()));
            if (Objects.nonNull(one)) {
                UniversalPanelVo copying = ObjectUtils.copying(one, UniversalPanelVo.class);
                businessPanelConfigVO.setUniversalPanelVo(copying);
            }
        }
        return businessPanelConfigVOS;
    }

    /**
     * 区域场景配置列表(根据区域主键查询)
     *
     * @param areaId    区域主键
     * @param projectId 项目id
     *
     * @return
     */
    @GetMapping("/area-scene-configs/area/{areaId}")
    public List<SceneTypeVO> listAreaSceneConfigsByAreaId(@PathVariable("areaId") Long areaId,
                                                          @RequestParam Long projectId
                                                         ) {
        List<AreaSceneConfig> areaSceneConfigs = this.areaSceneConfigService.listAreaSceneConfigsByAreaId(projectId, areaId);
        if (CollectionUtils.isEmpty(areaSceneConfigs)) {
            return Collections.emptyList();
        }
        List<Long> sceneTypeIds = areaSceneConfigs.stream().map(AreaSceneConfig::getSceneType).collect(Collectors.toList());
        List<SceneType> sceneTypes = this.sceneTypeService.getByIds(projectId, sceneTypeIds);
        return ObjectUtils.copying(sceneTypes, SceneTypeVO.class);
    }

    /**
     * 三维可视化通过区域获取场景列表
     *
     * @param areaId
     * @param sceneType
     *
     * @return
     */
    @GetMapping("/area-scene-configs/bim/scenes/{areaId}/{sceneType}")
    public List<SceneTypeBimVO> listBimSceneConfigByAreaId(@PathVariable("areaId") Long areaId, @PathVariable("sceneType") Long sceneType) {
        SceneType type = sceneTypeService.getById(sceneType);
        List<AreaSceneConfig> areaSceneConfigs = this.areaSceneConfigService.listAreaSceneConfigsByAreaId(type.getProjectId(), areaId);
        if (CollectionUtils.isEmpty(areaSceneConfigs)) {
            return Collections.emptyList();
        }
        List<Long> sceneTypeIds = areaSceneConfigs.stream().map(AreaSceneConfig::getSceneType).collect(Collectors.toList());
        List<SceneType> sceneTypes = this.sceneTypeService.list(Wrappers.<SceneType>lambdaQuery().eq(SceneType::getParentId, sceneType)
                                                                        .in(SceneType::getId, sceneTypeIds));
        List<SceneTypeBimVO> sceneTypeVOS = ObjectUtils.copying(sceneTypes, SceneTypeBimVO.class);
        if (!CollectionUtils.isEmpty(sceneTypeVOS)) {
            sceneTypeVOS.forEach(sceneTypeVO -> {
                List<FileVO> fileVOS = this.fileClient.getByBusinessKey(sceneTypeVO.getId());
                if (!CollectionUtils.isEmpty(fileVOS)) {
                    sceneTypeVO.setFileVO(fileVOS.get(0));
                }
            });
        }
        return sceneTypeVOS;
    }

    /**
     * 三维可视化通过区域获取场景列表
     *
     * @param areaId
     * @param projectId 项目id
     *
     * @return
     */
    @GetMapping("/area-scene-configs/bim/scenes/{areaId}")
    public List<SceneTypeBimVO> listBimSceneConfigByAreaIdProjectId(@PathVariable("areaId") Long areaId,
                                                                    @RequestParam Long projectId
                                                                   ) {
        List<AreaSceneConfig> areaSceneConfigs = this.areaSceneConfigService.listAreaSceneConfigsByAreaId(projectId, areaId);
        if (CollectionUtils.isEmpty(areaSceneConfigs)) {
            return Collections.emptyList();
        }
        List<Long> sceneTypeIds = areaSceneConfigs.stream().map(AreaSceneConfig::getSceneType).collect(Collectors.toList());
        List<SceneType> sceneTypes = this.sceneTypeService.list(Wrappers.<SceneType>lambdaQuery().in(SceneType::getId, sceneTypeIds));
        List<SceneTypeBimVO> sceneTypeVOS = ObjectUtils.copying(sceneTypes, SceneTypeBimVO.class);
        if (!CollectionUtils.isEmpty(sceneTypeVOS)) {
            sceneTypeVOS.forEach(sceneTypeVO -> {
                List<FileVO> fileVOS = this.fileClient.getByBusinessKey(sceneTypeVO.getId());
                if (!CollectionUtils.isEmpty(fileVOS)) {
                    sceneTypeVO.setFileVO(fileVOS.get(0));
                }
            });
        }
        return sceneTypeVOS;
    }

    /**
     * 区域场景配置列表(根据场景类型查询)
     *
     * @param sceneType 场景类型
     *
     * @return
     */
    @GetMapping("/area-scene-configs/scene/{sceneType}")
    public List<AreaVO> listAreaSceneConfigsBySceneType(@PathVariable("sceneType") Long sceneType) {
        List<AreaSceneConfig> areaSceneConfigs = this.areaSceneConfigService.listAreaSceneConfigsBySceneType(sceneType);
        if (CollectionUtils.isEmpty(areaSceneConfigs)) {
            return Collections.emptyList();
        }
        List<Long> areaIds = areaSceneConfigs.stream().map(AreaSceneConfig::getAreaId).collect(Collectors.toList());
        List<Area> areas = this.areaService.getByIds(areaIds);
        return ObjectUtils.copying(areas, AreaVO.class);
    }

    /**
     * 区域场景配置删除
     *
     * @param areaId    区域主键
     * @param sceneType 场景类型
     */
    @DeleteMapping("/area-scene-config/{areaId}/{sceneType}")
    public void remove(@PathVariable("areaId") Long areaId, @PathVariable("sceneType") Long sceneType) {
        this.areaSceneConfigService.remove(areaId, sceneType);
    }
}
