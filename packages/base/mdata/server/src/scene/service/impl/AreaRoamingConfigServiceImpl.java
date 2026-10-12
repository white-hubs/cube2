package com.guhecloud.cube.biz.mdata.server.scene.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.mdata.server.scene.dao.AreaRoamingConfigMapper;
import com.guhecloud.cube.biz.mdata.server.scene.entity.AreaRoamingConfig;
import com.guhecloud.cube.biz.mdata.server.scene.param.AreaRoamingConfigModifyParam;
import com.guhecloud.cube.biz.mdata.server.scene.param.AreaRoamingConfigQueryParam;
import com.guhecloud.cube.biz.mdata.server.scene.service.AreaRoamingConfigService;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import com.guhecloud.platform.base.util.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class AreaRoamingConfigServiceImpl extends ServiceImpl<AreaRoamingConfigMapper, AreaRoamingConfig> implements AreaRoamingConfigService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long save(@NonNull AreaRoamingConfigModifyParam param) {
        AreaRoamingConfig areaRoamingConfig = ObjectUtils.copying(param, AreaRoamingConfig.class);
        areaRoamingConfig.setAreaId(Long.valueOf(param.getAreaIds().get(param.getAreaIds().size() - 1)));
        areaRoamingConfig.setAreas(String.join("-", param.getAreaIds()));
        this.baseMapper.insert(areaRoamingConfig);
        return areaRoamingConfig.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(@NonNull Long id, @NonNull AreaRoamingConfigModifyParam param) {
        AreaRoamingConfig areaRoamingConfig = this.getById(id);
        Assert.isNotFound(areaRoamingConfig, "漫游信息不存在");
        ObjectUtils.copying(param, areaRoamingConfig);
        areaRoamingConfig.setAreaId(Long.valueOf(param.getAreaIds().get(param.getAreaIds().size() - 1)));
        areaRoamingConfig.setAreas(String.join("-", param.getAreaIds()));
        this.baseMapper.updateById(areaRoamingConfig);
    }

    @Override
    public List<AreaRoamingConfig> listAreaRoamingConfigs(AreaRoamingConfigQueryParam param) {
        return this.lambdaQuery()
                .like(StringUtils.isNotEmpty(param.getKeyword()), AreaRoamingConfig::getName, param.getKeyword())
                .eq(Objects.nonNull(param.getSceneType()), AreaRoamingConfig::getSceneType, param.getSceneType())
                .eq(Objects.nonNull(param.getAreaId()), AreaRoamingConfig::getAreaId, param.getAreaId())
                .orderByDesc(AreaRoamingConfig::getCreateTime)
                .list();
    }

    @Override
    public Page<AreaRoamingConfig> page(AreaRoamingConfigQueryParam param) {
        return this.lambdaQuery()
                .like(StringUtils.isNotEmpty(param.getKeyword()), AreaRoamingConfig::getName, param.getKeyword())
                .eq(Objects.nonNull(param.getSceneType()), AreaRoamingConfig::getSceneType, param.getSceneType())
                .eq(Objects.nonNull(param.getAreaId()), AreaRoamingConfig::getAreaId, param.getAreaId())
                .orderByDesc(AreaRoamingConfig::getCreateTime)
                .page(PagingUtils.convert(param));
    }

    @Override
    public AreaRoamingConfig getById(@NonNull Serializable id) {
        return this.baseMapper.selectById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void remove(@NonNull Long id) {
        DatabaseContextHolder.softDelete(this, id);
    }
}
