package com.guhecloud.cube.biz.mdata.server.area.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.mdata.server.area.dao.AreaGroupConfigMapper;
import com.guhecloud.cube.biz.mdata.server.area.entity.AreaGroupConfig;
import com.guhecloud.cube.biz.mdata.server.area.param.AreaGroupConfigModifyParam;
import com.guhecloud.cube.biz.mdata.server.area.service.AreaGroupConfigService;
import com.guhecloud.cube.biz.mdata.server.stat.param.AreaStatQueryParam;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.guhecloud.platform.base.util.CollectionUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * 区域组下区域服务实现
 *
 * @author lanchun
 * @date 2022/4/18 14:31
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AreaGroupConfigServiceImpl extends ServiceImpl<AreaGroupConfigMapper, AreaGroupConfig> implements AreaGroupConfigService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long save(@NonNull AreaGroupConfigModifyParam param) {
        AreaGroupConfig areaGroupConfig = this.getAreaGroupAreas(param.getAreaGroupId(), param.getAreaId());
        if (Objects.isNull(areaGroupConfig)) {
            //新增
            areaGroupConfig = ObjectUtils.copying(param, AreaGroupConfig.class);
            this.baseMapper.insert(areaGroupConfig);
        } else {
            //修改
            ObjectUtils.copying(param, areaGroupConfig);
            this.baseMapper.updateById(areaGroupConfig);
        }
        return areaGroupConfig.getId();
    }

    @Override
    public AreaGroupConfig getAreaGroupAreas(@NonNull Long areaGroupId, @NonNull Long areaId) {
        return this.baseMapper.selectOne(Wrappers.<AreaGroupConfig>lambdaQuery()
                                                 .eq(AreaGroupConfig::getAreaGroupId, areaGroupId)
                                                 .eq(AreaGroupConfig::getAreaId, areaId)
                                        );
    }

    @Override
    public AreaGroupConfig getAreaGroupAreas(@NonNull String areaGroup, @NonNull String area) {
        return this.baseMapper.selectOne(Wrappers.<AreaGroupConfig>lambdaQuery()
                                                 .eq(AreaGroupConfig::getAreaGroup, areaGroup)
                                                 .eq(AreaGroupConfig::getArea, area)
                                        );
    }

    @Override
    public List<AreaGroupConfig> listAreaGroupsAreas(@NonNull Long areaGroupId) {
        return this.lambdaQuery().eq(AreaGroupConfig::getAreaGroupId, areaGroupId).list();
    }

    @Override
    public List<AreaGroupConfig> listAreaGroupsAreas(@NonNull Collection<Long> areaGroupIds) {
        return CollectionUtils.isEmpty(areaGroupIds) ? Collections.emptyList() :
               this.lambdaQuery().in(AreaGroupConfig::getAreaGroupId, areaGroupIds).list();
    }

    @Override
    public Page<AreaGroupConfig> pageAreaGroupsStatAreas(@NonNull AreaStatQueryParam queryParam) {
        return this.lambdaQuery()
                .eq(AreaGroupConfig::getAreaGroupId, queryParam.getAreaGroupId())
                .page(PagingUtils.convert(queryParam));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void remove(@NonNull Long areaGroupId) {
        List<AreaGroupConfig> areaGroupAreas = this.listAreaGroupsAreas(areaGroupId);
        if (!CollectionUtils.isEmpty(areaGroupAreas)) {
            DatabaseContextHolder.softDelete(this, areaGroupAreas);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void remove(@NonNull Long areaGroupId, @NonNull Long areaId) {
        AreaGroupConfig areaGroupConfig = this.getAreaGroupAreas(areaGroupId, areaId);
        if (Objects.nonNull(areaGroupConfig)) {
            DatabaseContextHolder.softDelete(this, areaGroupConfig);
        }
    }
}
