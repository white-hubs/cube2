package com.guhecloud.cube.biz.mdata.server.scene.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.mdata.server.scene.dao.BusinessPanelConfigMapper;
import com.guhecloud.cube.biz.mdata.server.scene.entity.BusinessPanelConfig;
import com.guhecloud.cube.biz.mdata.server.scene.enumeration.BusinessPanelPositionEnum;
import com.guhecloud.cube.biz.mdata.server.scene.param.BusinessPanelConfigModifyParam;
import com.guhecloud.cube.biz.mdata.server.scene.service.BusinessPanelConfigService;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.guhecloud.platform.base.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * 设施面板配置服务实现
 *
 * @author lanchun
 * @date 2022/4/1 14:57
 */
@Slf4j
@Service
public class BusinessPanelConfigServiceImpl extends ServiceImpl<BusinessPanelConfigMapper, BusinessPanelConfig> implements BusinessPanelConfigService {

    /**
     * 新增
     *
     * @param param
     *
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long save(@NonNull BusinessPanelConfigModifyParam param) {
        BusinessPanelConfig businessPanelConfig = this.getByBusinessId(param.getProjectId(), param.getBusinessId(), param.getPanelPosition());
        if (Objects.nonNull(businessPanelConfig)) {
            //修改
            ObjectUtils.copying(param, businessPanelConfig);
            this.baseMapper.updateById(businessPanelConfig);
        } else {
            //新增
            businessPanelConfig = ObjectUtils.copying(param, BusinessPanelConfig.class);
            this.baseMapper.insert(businessPanelConfig);
        }
        return businessPanelConfig.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveBatch(@NonNull List<BusinessPanelConfigModifyParam> params) {
        if (CollectionUtils.isEmpty(params)) {
            return;
        }
        params.forEach(this::save);
    }

    /**
     * 业务主键查询面板配置
     *
     * @param businessId
     * @param projectId
     *
     * @return
     */
    @Override
    public BusinessPanelConfig getByBusinessId(@NonNull Long projectId, @NonNull Long businessId) {
        List<BusinessPanelConfig> businessPanelConfigs = this.listByBusinessId(projectId, businessId);
        return !CollectionUtils.isEmpty(businessPanelConfigs) ? businessPanelConfigs.get(0) : null;
    }

    /**
     * 业务主键面板定位查询面板配置
     *
     * @param businessId
     * @param panelPosition
     * @param projectId
     *
     * @return
     */
    @Override
    public BusinessPanelConfig getByBusinessId(@NonNull Long projectId, @NonNull Long businessId,
                                               @NonNull BusinessPanelPositionEnum panelPosition
                                              ) {
        return this.getOne(Wrappers.<BusinessPanelConfig>lambdaQuery()
                                   .eq(BusinessPanelConfig::getBusinessId, businessId)
                                   .eq(BusinessPanelConfig::getPanelPosition, panelPosition)
                                   .eq(BusinessPanelConfig::getProjectId, projectId)
                          );
    }

    /**
     * 业务主键查询面板配置集合
     *
     * @param businessId
     * @param projectId
     *
     * @return
     */
    @Override
    public List<BusinessPanelConfig> listByBusinessId(@NonNull Long projectId, @NonNull Long businessId) {
        return this.listByBusinessIds(projectId, Collections.singletonList(businessId));
    }

    /**
     * 业务主键集合查询面板配置集合
     *
     * @param businessIds
     * @param projectId
     *
     * @return
     */
    @Override
    public List<BusinessPanelConfig> listByBusinessIds(@NonNull Long projectId, @NonNull List<Long> businessIds) {
        return !CollectionUtils.isEmpty(businessIds) ? this.lambdaQuery()
                .eq(BusinessPanelConfig::getProjectId, projectId)
                .in(BusinessPanelConfig::getBusinessId, businessIds).list() : Collections.emptyList();
    }

    /**
     * 面板配置删除
     *
     * @param businessId
     * @param projectId
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void remove(@NonNull Long projectId, @NonNull Long businessId) {
        removeByBusinessId(projectId, businessId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeByBusinessId(@NonNull Long projectId, @NonNull Long businessId) {
        List<BusinessPanelConfig> businessPanelConfigs = this.listByBusinessId(projectId, businessId);
        if (!CollectionUtils.isEmpty(businessPanelConfigs)) {
            DatabaseContextHolder.softDelete(this, businessPanelConfigs);
        }
    }

    /**
     * 面板配置删除
     *
     * @param projectId
     * @param businessId
     * @param panelPosition
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void remove(@NonNull Long projectId, @NonNull Long businessId, @NonNull BusinessPanelPositionEnum panelPosition) {
        BusinessPanelConfig businessPanelConfig = this.getByBusinessId(projectId, businessId, panelPosition);
        if (Objects.nonNull(businessPanelConfig)) {
            DatabaseContextHolder.softDelete(this, businessPanelConfig);
        }
    }
}
