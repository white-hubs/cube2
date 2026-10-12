package com.guhecloud.cube.biz.daily.task.configuration.service.Impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.daily.task.configuration.dao.DailyTaskCategoryMapper;
import com.guhecloud.cube.biz.daily.task.configuration.param.DailyTaskCategoryInsertParam;
import com.guhecloud.cube.biz.daily.task.configuration.param.DailyTaskCategoryPageParam;
import com.guhecloud.cube.biz.daily.task.configuration.param.DailyTaskCategoryUpdateParam;
import com.guhecloud.cube.biz.daily.task.configuration.response.DailyTaskCategoryResponse;
import com.guhecloud.cube.biz.daily.task.configuration.service.DailyTaskCategoryService;
import com.guhecloud.cube.biz.daily.task.domain.po.DailyTaskCategoryPO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 任务类型
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DailyTaskCategoryServiceImpl extends ServiceImpl<DailyTaskCategoryMapper, DailyTaskCategoryPO> implements DailyTaskCategoryService {

    /**
     * 新增任务类型
     *
     * @param param 入参
     *
     * @return 主键ID
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long insertDailyTaskCategory(DailyTaskCategoryInsertParam param) {
        //检查名称是否存在
        Assert.isConflict(this.lambdaQuery().eq(DailyTaskCategoryPO::getDailyType, param.getDailyType())
                                  .eq(DailyTaskCategoryPO::getName, param.getName()).exists(), "名称已存在");
        var dailyTaskCategoryPO = new DailyTaskCategoryPO();
        dailyTaskCategoryPO.setId(IdWorker.getId());
        dailyTaskCategoryPO.setName(param.getName());
        dailyTaskCategoryPO.setDailyType(param.getDailyType());
        this.save(dailyTaskCategoryPO);
        return dailyTaskCategoryPO.getId();
    }

    /**
     * 删除任务类型
     *
     * @param id 主键ID
     *
     * @return 是否成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteDailyTaskCategory(Long id) {
        var dailyTaskCategoryPO = this.getById(id);
        Assert.isNotFound(dailyTaskCategoryPO, "未查询到相关信息");
        return this.removeById(id);
    }

    /**
     * 修改任务类型
     *
     * @param param 入参
     *
     * @return 是否成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateDailyTaskCategory(DailyTaskCategoryUpdateParam param) {
        //检查名称是否存在
        Assert.isConflict(this.lambdaQuery()
                                  .eq(DailyTaskCategoryPO::getDailyType, param.getDailyType())
                                  .eq(DailyTaskCategoryPO::getName, param.getName())
                                  .ne(DailyTaskCategoryPO::getId, param.getId())
                                  .exists(), "名称已存在");
        var dailyTaskCategoryPO = this.getById(param.getId());
        Assert.isNotFound(dailyTaskCategoryPO, "未查询到相关信息");
        if (param.getName() != null) {
            dailyTaskCategoryPO.setName(param.getName());
        }
        if (param.getDailyType() != null) {
            dailyTaskCategoryPO.setDailyType(param.getDailyType());
        }
        return this.updateById(dailyTaskCategoryPO);
    }

    /**
     * 查询任务类型分页
     *
     * @param param 查询条件
     *
     * @return 分页列表
     */
    @Override
    public Pagination<DailyTaskCategoryResponse> queryDailyTaskCategoryPage(DailyTaskCategoryPageParam param) {
        var page = this.page(
                PagingUtils.convert(param),
                Wrappers.<DailyTaskCategoryPO>lambdaQuery()
                        .like(StringUtils.isNoneBlank(param.getName()), DailyTaskCategoryPO::getName, param.getName())
                        .eq(StringUtils.isNoneBlank(param.getDailyType()), DailyTaskCategoryPO::getDailyType, param.getDailyType())
                        .orderByAsc(DailyTaskCategoryPO::getCreateTime)
                            );
        return PagingUtils.convert(page, DailyTaskCategoryResponse.class);
    }

    @Override
    public Map<Long, DailyTaskCategoryPO> getCategoryByIds(Set<Long> taskCategoryIds) {
        if (CollectionUtils.isEmpty(taskCategoryIds)) {
            return new HashMap<>();
        }
        return Optional.ofNullable(this.lambdaQuery().in(DailyTaskCategoryPO::getId, taskCategoryIds).list())
                .orElse(new ArrayList<>())
                .stream().collect(Collectors.toMap(DailyTaskCategoryPO::getId, dailyTaskCategoryPO -> dailyTaskCategoryPO));

    }
}
