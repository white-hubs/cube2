package com.guhecloud.cube.biz.daily.task.configuration.service.Impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.daily.task.domain.po.DailyUrgencyPO;
import com.guhecloud.cube.biz.daily.task.configuration.dao.DailyUrgencyMapper;
import com.guhecloud.cube.biz.daily.task.configuration.param.DailyUrgencyInsertParam;
import com.guhecloud.cube.biz.daily.task.configuration.param.DailyUrgencyPageParam;
import com.guhecloud.cube.biz.daily.task.configuration.param.DailyUrgencyUpdateParam;
import com.guhecloud.cube.biz.daily.task.configuration.response.DailyUrgencyResponse;
import com.guhecloud.cube.biz.daily.task.configuration.service.DailyUrgencyService;
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
 * 紧急程度
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DailyUrgencyServiceImpl extends ServiceImpl<DailyUrgencyMapper, DailyUrgencyPO> implements DailyUrgencyService {

    /**
     * 新增紧急程度
     *
     * @param param 入参
     *
     * @return 主键ID
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long insertDailyUrgency(DailyUrgencyInsertParam param) {
        var dailyUrgencyPO = new DailyUrgencyPO();
        dailyUrgencyPO.setId(IdWorker.getId());
        dailyUrgencyPO.setLevel(param.getLevel());
        dailyUrgencyPO.setName(param.getName());
        dailyUrgencyPO.setDailyType(param.getDailyType());
        this.save(dailyUrgencyPO);
        return dailyUrgencyPO.getId();
    }

    /**
     * 删除紧急程度
     *
     * @param id 主键ID
     *
     * @return 是否成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteDailyUrgency(Long id) {
        var dailyUrgencyPO = this.getById(id);
        Assert.isNotFound(dailyUrgencyPO, "未查询到相关信息");
        return this.removeById(id);
    }

    /**
     * 修改紧急程度
     *
     * @param param 入参
     *
     * @return 是否成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateDailyUrgency(DailyUrgencyUpdateParam param) {
        var dailyUrgencyPO = this.getById(param.getId());
        Assert.isNotFound(dailyUrgencyPO, "未查询到相关信息");
        if (param.getLevel() != null) {
            dailyUrgencyPO.setLevel(param.getLevel());
        }
        if (param.getName() != null) {
            dailyUrgencyPO.setName(param.getName());
        }
        if (param.getDailyType() != null) {
            dailyUrgencyPO.setDailyType(param.getDailyType());
        }
        return this.updateById(dailyUrgencyPO);
    }

    /**
     * 查询紧急程度分页
     *
     * @param param 查询条件
     *
     * @return 分页列表
     */
    @Override
    public Pagination<DailyUrgencyResponse> queryDailyUrgencyPage(DailyUrgencyPageParam param) {
        var page = this.page(
                PagingUtils.convert(param),
                Wrappers.<DailyUrgencyPO>lambdaQuery()
                        .like(StringUtils.isNoneBlank(param.getName()), DailyUrgencyPO::getName, param.getName())
                        .eq(StringUtils.isNoneBlank(param.getDailyType()), DailyUrgencyPO::getDailyType, param.getDailyType())
                        .orderByAsc(DailyUrgencyPO::getLevel)
                        .orderByDesc(DailyUrgencyPO::getCreateTime)
                            );
        return PagingUtils.convert(page, DailyUrgencyResponse.class);
    }

    @Override
    public Map<Long, DailyUrgencyPO> getUrgencyByIds(Set<Long> urgencyIds) {
        if (CollectionUtils.isEmpty(urgencyIds)) {
            return new HashMap<>();
        }
        return Optional.ofNullable(this.lambdaQuery().in(DailyUrgencyPO::getId, urgencyIds).list())
                .orElse(new ArrayList<>())
                .stream().collect(Collectors.toMap(DailyUrgencyPO::getId, dailyUrgencyPO -> dailyUrgencyPO));
    }
}
