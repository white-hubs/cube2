package com.guhecloud.cube.biz.mdata.server.scene.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.mdata.server.scene.dao.BusinessSceneConfigMapper;
import com.guhecloud.cube.biz.mdata.server.scene.entity.BusinessSceneConfig;
import com.guhecloud.cube.biz.mdata.server.scene.entity.SceneType;
import com.guhecloud.cube.biz.mdata.server.scene.enumeration.BusinessSceneConfigTypeEnum;
import com.guhecloud.cube.biz.mdata.server.scene.param.StallSceneConfigModifyParam;
import com.guhecloud.cube.biz.mdata.server.scene.service.SceneTypeService;
import com.guhecloud.cube.biz.mdata.server.scene.service.StallSceneConfigService;
import com.guhecloud.cube.biz.mdata.server.stall.service.StallService;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * 档位场景配置实现
 *
 * @author lanchun
 * @date 2021/12/2 14:03
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class StallSceneConfigServiceImpl extends ServiceImpl<BusinessSceneConfigMapper, BusinessSceneConfig> implements StallSceneConfigService {

    private final SceneTypeService sceneTypeService;

    private final StallService stallService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(@NonNull StallSceneConfigModifyParam param) {
        Set<Long> stallIds = stallService.getStallsBySceneConfig(param);
        if (com.guhecloud.platform.base.util.CollectionUtils.isEmpty(stallIds)) {
            return;
        }
        SceneType sceneType = this.sceneTypeService.getById(param.getSceneType());
        this.remove(stallIds, param.getSceneType());
        for (Long stallId : stallIds) {
            //新增
            BusinessSceneConfig businessSceneConfig = new BusinessSceneConfig();
            businessSceneConfig.setProjectId(sceneType.getProjectId());
            businessSceneConfig.setBusinessId(stallId);
            businessSceneConfig.setBusinessType(BusinessSceneConfigTypeEnum.STALL);
            businessSceneConfig.setSceneType(sceneType.getId());
            businessSceneConfig.setSceneTypeSequence(sceneType.getSequence());
            this.baseMapper.insert(businessSceneConfig);
        }
    }

    @Override
    public List<BusinessSceneConfig> listBusinessSceneConfigByBusinessId(@NonNull Long projectId, @NonNull Long stallId) {
        return this.lambdaQuery()
                .eq(BusinessSceneConfig::getProjectId, projectId)
                .eq(BusinessSceneConfig::getBusinessId, stallId).list();
    }

    @Override
    public BusinessSceneConfig getByBusinessScene(@NonNull Long stallId, @NonNull Long sceneType) {
        return this.baseMapper.selectOne(Wrappers.<BusinessSceneConfig>lambdaQuery().eq(BusinessSceneConfig::getBusinessId, stallId)
                                                 .eq(BusinessSceneConfig::getSceneType, sceneType));
    }

    @Override
    public void removeByProjectId(@NonNull Long projectId, @NonNull Long stallId) {
        List<BusinessSceneConfig> businessSceneConfigs = this.listBusinessSceneConfigByBusinessId(projectId, stallId);
        if (!CollectionUtils.isEmpty(businessSceneConfigs)) {
            DatabaseContextHolder.softDelete(this, businessSceneConfigs);
        }
    }

    @Override
    public void remove(@NonNull Long stallId, @NonNull Long sceneType) {
        BusinessSceneConfig businessSceneConfig = this.getByBusinessScene(stallId, sceneType);
        if (Objects.nonNull(businessSceneConfig)) {
            DatabaseContextHolder.softDelete(this, businessSceneConfig);
        }
    }

    @Override
    public void remove(@NonNull Set<Long> stallIds, @NonNull Long sceneType) {
        if (!com.guhecloud.platform.base.util.CollectionUtils.isEmpty(stallIds)) {
            this.baseMapper.delete(Wrappers.<BusinessSceneConfig>lambdaQuery()
                                           .in(BusinessSceneConfig::getBusinessId, stallIds)
                                           .eq(BusinessSceneConfig::getSceneType, sceneType));
        }
    }
}
