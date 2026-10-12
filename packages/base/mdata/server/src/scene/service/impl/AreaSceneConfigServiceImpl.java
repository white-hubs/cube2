package com.guhecloud.cube.biz.mdata.server.scene.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.mdata.server.scene.dao.AreaSceneConfigMapper;
import com.guhecloud.cube.biz.mdata.server.scene.entity.AreaSceneConfig;
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
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * 区域场景配置实现
 *
 * @author lanchun
 * @date 2021/12/2 14:02
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AreaSceneConfigServiceImpl extends ServiceImpl<AreaSceneConfigMapper, AreaSceneConfig> implements AreaSceneConfigService {

    private final SceneTypeService sceneTypeService;

    private final BusinessPanelConfigService businessPanelConfigService;

    private final UniversalPanelService universalPanelService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long save(@NonNull AreaSceneConfigModifyParam param) {
        String bimBindViewIds = null;
        if (!CollectionUtils.isEmpty(param.getBimViewBinds())) {
            bimBindViewIds = String.join(",", param.getBimViewBinds());
        }
        //根据区域加场景查询是否存在配置
        AreaSceneConfig areaSceneConfig = this.getByAreaScene(param.getAreaId(), param.getSceneType());
        if (Objects.isNull(areaSceneConfig)) {
            //新增
            areaSceneConfig = ObjectUtils.copying(param, AreaSceneConfig.class);
            areaSceneConfig.setBimViewBindIds(bimBindViewIds);
            this.baseMapper.insert(areaSceneConfig);
        } else {
            //修改
            ObjectUtils.copying(param, areaSceneConfig);
            areaSceneConfig.setBimViewBindIds(bimBindViewIds);
            this.baseMapper.updateById(areaSceneConfig);
        }
        return areaSceneConfig.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void disposableSave(AreaScenePanelConfigBatchModifyParam param) {
        SceneType sceneType = sceneTypeService.getById(param.getSceneType());
        Long areaSceneConfigId = this.save(new AreaSceneConfigModifyParam(param.getAreaId(), sceneType.getProjectId(), param.getSceneType(), sceneType.getSequence(), Collections.emptyList()));
        //删除面板配置
        this.businessPanelConfigService.removeByBusinessId(sceneType.getProjectId(), areaSceneConfigId);
        if (!CollectionUtils.isEmpty(param.getPanelConfigModifyParams())) {
            //保存面板配置
            List<BusinessPanelConfigModifyParam> panelConfigModifyParams = ObjectUtils.copying(param.getPanelConfigModifyParams(), BusinessPanelConfigModifyParam.class);
            panelConfigModifyParams.forEach(panelConfigModifyParam -> {
                panelConfigModifyParam.setProjectId(sceneType.getProjectId());
                panelConfigModifyParam.setBusinessId(areaSceneConfigId);
                panelConfigModifyParam.setBusinessType(BusinessPanelConfigTypeEnum.AREA_SCENE);
                Long save = this.businessPanelConfigService.save(panelConfigModifyParam);
                UniversalPanel one = universalPanelService.getOne(Wrappers.<UniversalPanel>lambdaQuery()
                                                                          .eq(UniversalPanel::getBusinessId, save));
                if (Objects.nonNull(one)) {
                    DatabaseContextHolder.softDelete(universalPanelService, one.getId());
                    if (Objects.nonNull(panelConfigModifyParam.getUniversalPanelParam())) {
                        UniversalPanel copying = ObjectUtils.copying(panelConfigModifyParam.getUniversalPanelParam(), UniversalPanel.class);
                        copying.setProjectId(sceneType.getProjectId());
                        copying.setBusinessId(save);
                        universalPanelService.save(copying);
                    }

                } else {
                    if (Objects.nonNull(panelConfigModifyParam.getUniversalPanelParam())) {
                        UniversalPanel copying = ObjectUtils.copying(panelConfigModifyParam.getUniversalPanelParam(), UniversalPanel.class);
                        copying.setProjectId(sceneType.getProjectId());
                        copying.setBusinessId(save);
                        universalPanelService.save(copying);
                    }

                }

            });
        }
    }

    @Override
    public List<AreaSceneConfig> listAreaSceneConfigsByAreaId(@NonNull Long projectId, @NonNull Long areaId) {
        return this.lambdaQuery().eq(AreaSceneConfig::getAreaId, areaId)
                .eq(AreaSceneConfig::getProjectId, projectId).list();
    }

    @Override
    public List<AreaSceneConfig> listAreaSceneConfigsBySceneType(@NonNull Long sceneType) {
        return this.lambdaQuery().eq(AreaSceneConfig::getSceneType, sceneType).list();
    }

    @Override
    public AreaSceneConfig getByAreaScene(@NonNull Long areaId, @NonNull Long sceneType) {
        return this.baseMapper.selectOne(Wrappers.<AreaSceneConfig>lambdaQuery().eq(AreaSceneConfig::getAreaId, areaId)
                                                 .eq(AreaSceneConfig::getSceneType, sceneType));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void remove(@NonNull Long areaId, @NonNull Long sceneType) {
        AreaSceneConfig areaSceneConfig = this.getByAreaScene(areaId, sceneType);
        if (Objects.nonNull(areaSceneConfig)) {
            DatabaseContextHolder.softDelete(this, areaSceneConfig);
        }
    }
}
