package com.guhecloud.cube.biz.work.center.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.work.center.server.dao.WorkBusinessUrlMapper;
import com.guhecloud.cube.biz.work.center.server.entity.WorkBusinessUrl;
import com.guhecloud.cube.biz.work.center.server.param.WorkBusinessUrlModifyParam;
import com.guhecloud.cube.biz.work.center.server.service.WorkBusinessUrlService;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WorkBusinessUrlServiceImpl extends ServiceImpl<WorkBusinessUrlMapper, WorkBusinessUrl> implements WorkBusinessUrlService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long save(@NonNull WorkBusinessUrlModifyParam param) {
        WorkBusinessUrl workBusinessUrl = ObjectUtils.copying(param, WorkBusinessUrl.class);
        this.baseMapper.insert(workBusinessUrl);
        return workBusinessUrl.getId();
    }

    @Override
    public void update(@NonNull Long id, @NonNull WorkBusinessUrlModifyParam param) {
        WorkBusinessUrl workBusinessUrl = this.getById(id);
        if (Objects.nonNull(workBusinessUrl)) {
            ObjectUtils.copying(param, workBusinessUrl);
            this.baseMapper.updateById(workBusinessUrl);
        }
    }

    @Override
    public WorkBusinessUrl getById(@NonNull Serializable id) {
        return this.baseMapper.selectById(id);
    }

    @Override
    public List<WorkBusinessUrl> listByBusinessTypes(@NonNull Collection<String> businessTypes, @NonNull String terminal) {
        return CollectionUtils.isEmpty(businessTypes) ? Collections.emptyList() : this.lambdaQuery()
                .in(WorkBusinessUrl::getBusinessType, businessTypes)
                .eq(WorkBusinessUrl::getTerminal, terminal)
                .list();
    }
}
