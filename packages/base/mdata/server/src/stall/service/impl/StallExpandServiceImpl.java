package com.guhecloud.cube.biz.mdata.server.stall.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.mdata.server.stall.dao.StallExpandMapper;
import com.guhecloud.cube.biz.mdata.server.stall.entity.StallExpand;
import com.guhecloud.cube.biz.mdata.server.stall.param.StallExpandModifyParam;
import com.guhecloud.cube.biz.mdata.server.stall.service.StallExpandService;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.guhecloud.platform.base.util.CollectionUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class StallExpandServiceImpl extends ServiceImpl<StallExpandMapper, StallExpand> implements StallExpandService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long save(@NonNull Long stallId, StallExpandModifyParam param) {
        if (Objects.isNull(param)) {
            return 0L;
        }
        StallExpand stallExpand = this.getByStallId(stallId);
        if (Objects.isNull(stallExpand)) {
            stallExpand = ObjectUtils.copying(param, StallExpand.class);
            stallExpand.setStallId(stallId);
            this.baseMapper.insert(stallExpand);
        } else {
            ObjectUtils.copying(param, stallExpand);
            stallExpand.setStallId(stallId);
            this.baseMapper.updateById(stallExpand);
        }
        return stallExpand.getId();
    }

    @Override
    public StallExpand getByStallId(@NonNull Long stallId) {
        return this.baseMapper.selectOne(Wrappers.<StallExpand>lambdaQuery()
                                                 .eq(StallExpand::getStallId, stallId)
                                        );
    }

    @Override
    public List<StallExpand> getStallExpands(@NonNull Collection<Long> stallIds) {
        return CollectionUtils.isEmpty(stallIds) ? Collections.emptyList() : this.baseMapper
                .selectList(Wrappers.<StallExpand>lambdaQuery().in(StallExpand::getStallId, stallIds));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeByStallId(@NonNull Long stallId) {
        StallExpand stallExpand = this.getByStallId(stallId);
        if (Objects.nonNull(stallExpand)) {
            DatabaseContextHolder.softDelete(this, stallExpand);
        }
    }
}
