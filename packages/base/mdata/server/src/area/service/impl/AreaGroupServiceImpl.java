package com.guhecloud.cube.biz.mdata.server.area.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.mdata.server.area.dao.AreaGroupMapper;
import com.guhecloud.cube.biz.mdata.server.area.entity.AreaGroup;
import com.guhecloud.cube.biz.mdata.server.area.param.AreaGroupModifyParam;
import com.guhecloud.cube.biz.mdata.server.area.param.AreaGroupQueryParam;
import com.guhecloud.cube.biz.mdata.server.area.service.AreaGroupConfigService;
import com.guhecloud.cube.biz.mdata.server.area.service.AreaGroupService;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

/**
 * @author lanchun
 * @date 2022/4/15 14:52
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AreaGroupServiceImpl extends ServiceImpl<AreaGroupMapper, AreaGroup> implements AreaGroupService {

    private final AreaGroupConfigService areaGroupConfigService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long save(@NonNull AreaGroupModifyParam param) {
        AreaGroup areaGroup = ObjectUtils.copying(param, AreaGroup.class);
        this.baseMapper.insert(areaGroup);
        return areaGroup.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(@NonNull Long id, @NonNull AreaGroupModifyParam param) {
        AreaGroup areaGroup = this.getById(id);
        Assert.isNotFound(areaGroup, "不存在区域组信息");
        ObjectUtils.copying(param, areaGroup);
        this.baseMapper.updateById(areaGroup);
    }

    @Override
    public AreaGroup getById(@NonNull Serializable id) {
        return this.baseMapper.selectById(id);
    }

    @Override
    public List<AreaGroup> listAreaGroups(AreaGroupQueryParam param) {
        return this.lambdaQuery()
                .like(StringUtils.isNotEmpty(param.getKeyword()), AreaGroup::getName, param.getKeyword())
                .list();
    }

    @Override
    public List<AreaGroup> getSequence(@NonNull List<String> sequences) {
        return this.baseMapper.getBySequences(sequences);
    }

    @Override
    public List<AreaGroup> getByParentId(@NonNull Long parentId) {
        return this.lambdaQuery().eq(AreaGroup::getParentId, parentId).list();
    }

    @Override
    public List<AreaGroup> getFirstParent() {
        return this.lambdaQuery().isNull(AreaGroup::getParentId).list();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void remove(@NonNull Long id) {
        List<AreaGroup> areaGroups = this.getByParentId(id);
        Assert.isParameterError(CollectionUtils.isNotEmpty(areaGroups), "存在下级区域组");
        DatabaseContextHolder.softDelete(this, id);
        areaGroupConfigService.remove(id);
    }
}
