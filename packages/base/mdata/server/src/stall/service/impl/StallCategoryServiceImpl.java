package com.guhecloud.cube.biz.mdata.server.stall.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.file.client.FileRefClient;
import com.guhecloud.cube.biz.mdata.server.stall.dao.StallCategoryMapper;
import com.guhecloud.cube.biz.mdata.server.stall.entity.Stall;
import com.guhecloud.cube.biz.mdata.server.stall.entity.StallCategory;
import com.guhecloud.cube.biz.mdata.server.stall.param.StallCategoryCreateBatchParam;
import com.guhecloud.cube.biz.mdata.server.stall.param.StallCategoryCreateParam;
import com.guhecloud.cube.biz.mdata.server.stall.param.StallCategoryQueryParam;
import com.guhecloud.cube.biz.mdata.server.stall.param.StallCategoryUpdateParam;
import com.guhecloud.cube.biz.mdata.server.stall.service.StallCategoryService;
import com.guhecloud.cube.biz.mdata.server.stall.service.StallService;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class StallCategoryServiceImpl extends ServiceImpl<StallCategoryMapper, StallCategory> implements StallCategoryService {

    private final StallService stallService;

    private final FileRefClient fileRefClient;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(@NonNull StallCategoryCreateParam param) {
        Assert.isConflict(Objects.nonNull(this.getByName(param.getName())), "存在相同档位类型");
        StallCategory stallCategory = ObjectUtils.copying(param, StallCategory.class);
        stallCategory.setId(null);
        this.save(stallCategory);
        if (Objects.nonNull(param.getFileId())) {
            this.fileRefClient.save(stallCategory.getId(), param.getFileId());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(@NonNull StallCategoryUpdateParam param) {
        StallCategory stallCategory = this.getById(param.getId());
        Assert.isNotFound(stallCategory, "不存在档位类型信息");
        StallCategory stallCategoryByName = this.getByName(param.getName());
        Assert.isConflict(
                Objects.nonNull(stallCategoryByName) && !stallCategoryByName.getId().equals(param.getId()),
                "存在相同档位类型信息"
                         );
        ObjectUtils.copying(param, stallCategory);
        this.updateById(stallCategory);
        this.fileRefClient.removeByBusinessKey(stallCategory.getId());
        if (Objects.nonNull(param.getFileId())) {
            this.fileRefClient.save(stallCategory.getId(), param.getFileId());
        }
    }

    @Override
    public Page<StallCategory> page(@NonNull StallCategoryQueryParam queryParam) {
        return this.lambdaQuery()
                .eq(Objects.nonNull(queryParam.getEnabled()), StallCategory::getEnabled, queryParam.getEnabled())
                .like(StringUtils.isNotEmpty(queryParam.getKeyword()), StallCategory::getName, queryParam.getKeyword())
                .page(PagingUtils.convert(queryParam));
    }

    @Override
    public List<StallCategory> list(StallCategoryQueryParam queryParam) {
        return this.lambdaQuery()
                .like(StringUtils.isNotEmpty(queryParam.getKeyword()), StallCategory::getName, queryParam.getKeyword())
                .eq(Objects.nonNull(queryParam.getEnabled()), StallCategory::getEnabled, queryParam.getEnabled())
                .list();
    }

    @Override
    public void remove(@NonNull Long id) {
        List<Stall> stalls = stallService.getByCategoryId(id);
        Assert.isConflict(CollectionUtils.isNotEmpty(stalls), "档位存在该档位类型");
        DatabaseContextHolder.softDelete(this, id);
    }

    @Override
    public StallCategory getByName(@NonNull String name) {
        return this.lambdaQuery().eq(StallCategory::getName, name).one();
    }

    @Override
    public List<StallCategory> getByIds(@NonNull List<Long> ids) {
        return CollectionUtils.isEmpty(ids) ? Collections.emptyList() :
               this.lambdaQuery().in(StallCategory::getId, ids).list();
    }

    @Override
    public void saveBatchOnlyEntity(@NonNull StallCategoryCreateBatchParam param) {
        List<StallCategoryCreateParam> paramList = param.getCategoryList();
        if (paramList == null || paramList.isEmpty()) {
            return;
        }
        Map<String, StallCategory> oldMap = new HashMap<>();
        List<StallCategory> oldList = this.list();
        if (oldList != null) {
            for (StallCategory stallCategory : oldList) {
                if (StringUtils.isNotEmpty(stallCategory.getName())) {
                    oldMap.put(stallCategory.getName(), stallCategory);
                }
            }
        }
        List<StallCategory> insertList = new ArrayList<>();
        for (StallCategoryCreateParam paramCategory : paramList) {
            // 原来没有才新增
            if (oldMap.get(paramCategory.getName()) == null) {
                StallCategory stallCategory = ObjectUtils.copying(paramCategory, StallCategory.class);
                insertList.add(stallCategory);
            }
        }
        if (!insertList.isEmpty()) {
            this.saveBatch(insertList, 1000);
        }
    }
}
