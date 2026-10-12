package com.guhecloud.cube.biz.ouaa.server.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.ouaa.server.dao.ApplicationGroupMapper;
import com.guhecloud.cube.biz.ouaa.server.dao.ApplicationMapper;
import com.guhecloud.cube.biz.ouaa.server.entity.Application;
import com.guhecloud.cube.biz.ouaa.server.entity.ApplicationGroup;
import com.guhecloud.cube.biz.ouaa.server.param.ApplicationGroupModifyParam;
import com.guhecloud.cube.biz.ouaa.server.param.ApplicationGroupQueryParam;
import com.guhecloud.cube.biz.ouaa.server.service.ApplicationGroupService;
import com.guhecloud.cube.biz.ouaa.server.vo.BaseInfo;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 应用组业务操作实现
 *
 * @author lanchun
 * @date 2022/5/9 10:06
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ApplicationGroupServiceImpl extends ServiceImpl<ApplicationGroupMapper, ApplicationGroup> implements ApplicationGroupService {
    private final ApplicationMapper applicationMapper;

    @Override
    public Long save(@NonNull ApplicationGroupModifyParam param) {
        ApplicationGroup applicationGroup = ObjectUtils.copying(param, ApplicationGroup.class);
        this.baseMapper.insert(applicationGroup);
        return applicationGroup.getId();
    }

    @Override
    public void update(@NonNull Long id, @NonNull ApplicationGroupModifyParam param) {
        ApplicationGroup applicationGroup = this.getById(id);
        Assert.isNotFound(applicationGroup, "应用组不存在");
        ObjectUtils.copying(param, applicationGroup);
        this.baseMapper.updateById(applicationGroup);
    }

    @Override
    public ApplicationGroup getById(Serializable id) {
        return this.baseMapper.selectById(id);
    }

    @Override
    public List<ApplicationGroup> listByIds(Collection<? extends Serializable> ids) {
        return CollectionUtils.isEmpty(ids) ? Collections.emptyList() : this.lambdaQuery().in(ApplicationGroup::getId, ids).list();
    }

    @Override
    public Page<ApplicationGroup> listByPage(ApplicationGroupQueryParam param) {
        return this.lambdaQuery()
                .like(StringUtils.isNotEmpty(param.getKeyword()), ApplicationGroup::getName, param)
                .orderByDesc(ApplicationGroup::getCreateTime)
                .page(PagingUtils.convert(param));
    }

    @Override
    public List<BaseInfo> listForSelection(ApplicationGroupQueryParam param) {
        Set<Long> appGroupIdList = new HashSet<>();
        List<BaseInfo> baseInfoList = Optional.ofNullable(this.lambdaQuery()
                                                                  .like(StringUtils.isNotEmpty(param.getKeyword()), ApplicationGroup::getName, param)
                                                                  .list()).orElse(new ArrayList<>())
                .stream().map(applicationGroup -> {
                    BaseInfo baseInfo = new BaseInfo();
                    baseInfo.setId(applicationGroup.getId());
                    baseInfo.setName(applicationGroup.getName());
                    appGroupIdList.add(applicationGroup.getId());
                    return baseInfo;
                })
                .collect(Collectors.toList());
        List<Application> applications = applicationMapper.selectList(Wrappers.<Application>lambdaQuery()
                                                                              .select(Application::getApplicationGroupId)
                                                                              .eq(Application::getPublicAccess, true)
                                                                              .in(Application::getApplicationGroupId, appGroupIdList));
        if (CollectionUtils.isEmpty(applications)) {
            return new ArrayList<>();
        }
        Set<Long> existApplicationGroupIds = applications.stream().map(Application::getApplicationGroupId).collect(Collectors.toSet());
        baseInfoList.removeIf(baseInfo -> !existApplicationGroupIds.contains(baseInfo.getId()));
        return baseInfoList;
    }

    @Override
    public void remove(@NonNull Long id) {
        DatabaseContextHolder.softDelete(this, id);
    }

}
