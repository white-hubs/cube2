package com.guhecloud.cube.biz.work.center.server.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.work.center.server.dao.WorkTagMapper;
import com.guhecloud.cube.biz.work.center.server.entity.WorkTag;
import com.guhecloud.cube.biz.work.center.server.entity.WorkTagReferBusinessType;
import com.guhecloud.cube.biz.work.center.server.param.WorkTagModifyParam;
import com.guhecloud.cube.biz.work.center.server.param.WorkTagQueryParam;
import com.guhecloud.cube.biz.work.center.server.param.WorkTagReferBusinessTypeModifyParam;
import com.guhecloud.cube.biz.work.center.server.param.WorkTagUpdateParam;
import com.guhecloud.cube.biz.work.center.server.service.WorkTagReferBusinessTypeService;
import com.guhecloud.cube.biz.work.center.server.service.WorkTagService;
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

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WorkTagServiceImpl extends ServiceImpl<WorkTagMapper, WorkTag> implements WorkTagService {

    private final WorkTagReferBusinessTypeService workTagReferBusinessTypeService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long save(@NonNull WorkTagModifyParam param) {
        WorkTag workTag = ObjectUtils.copying(param, WorkTag.class);
        this.baseMapper.insert(workTag);
        return workTag.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long saveReferBusinessType(@NonNull String tagCode, @NonNull WorkTagReferBusinessTypeModifyParam param) {
        WorkTagReferBusinessType workTagReferBusinessType = WorkTagReferBusinessType.builder()
                .workTagCode(tagCode)
                .businessType(param.getBusinessType())
                .build();
        workTagReferBusinessTypeService.save(workTagReferBusinessType);
        return workTagReferBusinessType.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(@NonNull Long id, @NonNull WorkTagUpdateParam param) {
        WorkTag workTag = this.getById(id);
        if (Objects.nonNull(workTag)) {
            workTag.setName(param.getName());
            this.baseMapper.updateById(workTag);
        }
    }

    @Override
    public List<WorkTag> listWorkTags(WorkTagQueryParam param) {
        return this.lambdaQuery()
                .and(StringUtils.isNotEmpty(param.getKeyword()), q -> q
                        .like(WorkTag::getCode, param.getKeyword())
                        .or()
                        .like(WorkTag::getName, param.getKeyword()))
                .list();
    }

    @Override
    public List<WorkTag> listWorkTagsByCodes(@NonNull Set<String> workTagCodes) {
        return CollectionUtils.isEmpty(workTagCodes) ? Collections.emptyList() : this.lambdaQuery()
                .in(WorkTag::getCode, workTagCodes)
                .list();
    }

    @Override
    public List<WorkTagReferBusinessType> listWorkTagReferBusinessTypesByCode(@NonNull String workTagCode) {
        return this.listWorkTagReferBusinessTypesByCodes(Collections.singleton(workTagCode));
    }

    @Override
    public List<WorkTagReferBusinessType> listWorkTagReferBusinessTypesByCodes(@NonNull Set<String> workTagCodes) {
        return CollectionUtils.isEmpty(workTagCodes) ? Collections.emptyList() : this.workTagReferBusinessTypeService.list(Wrappers
                                                                                                                                   .<WorkTagReferBusinessType>lambdaQuery()
                                                                                                                                   .in(WorkTagReferBusinessType::getWorkTagCode, workTagCodes));
    }

    @Override
    public List<WorkTagReferBusinessType> listWorkTagReferBusinessTypes(@NonNull Set<String> businessTypes) {
        return CollectionUtils.isEmpty(businessTypes) ? Collections.emptyList() : this.workTagReferBusinessTypeService.list(Wrappers
                                                                                                                                    .<WorkTagReferBusinessType>lambdaQuery()
                                                                                                                                    .in(WorkTagReferBusinessType::getBusinessType, businessTypes));
    }

    @Override
    public Set<String> listBusinessTypesByCode(@NonNull String workTagCode) {
        List<WorkTagReferBusinessType> workTagReferBusinessTypes = this.listWorkTagReferBusinessTypesByCode(workTagCode);
        if (CollectionUtils.isEmpty(workTagReferBusinessTypes)) {
            return Collections.emptySet();
        }
        return workTagReferBusinessTypes.stream().map(WorkTagReferBusinessType::getBusinessType).collect(Collectors.toSet());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeById(@NonNull Long id) {
        DatabaseContextHolder.softDelete(this, id);
    }

    @Override
    public Set<String> listHandoverBusinessTypes() {
        List<WorkTag> list = this.lambdaQuery().eq(WorkTag::getHandover, true)
                .list();
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptySet();
        }
        List<WorkTagReferBusinessType> workTagReferBusinessTypes = this.listWorkTagReferBusinessTypesByCodes(list.stream()
                                                                                                                     .map(WorkTag::getCode)
                                                                                                                     .collect(Collectors.toSet()));
        if (CollectionUtils.isEmpty(workTagReferBusinessTypes)) {
            return Collections.emptySet();
        }
        return workTagReferBusinessTypes.stream().map(WorkTagReferBusinessType::getBusinessType).collect(Collectors.toSet());
    }
}
