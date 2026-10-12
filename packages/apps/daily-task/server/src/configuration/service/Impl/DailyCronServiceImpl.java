package com.guhecloud.cube.biz.daily.task.configuration.service.Impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.daily.task.configuration.dao.DailyCronMapper;
import com.guhecloud.cube.biz.daily.task.configuration.param.DailyCronBatchDeleteParam;
import com.guhecloud.cube.biz.daily.task.configuration.param.DailyCronInsertParam;
import com.guhecloud.cube.biz.daily.task.configuration.param.DailyCronPageParam;
import com.guhecloud.cube.biz.daily.task.configuration.param.DailyCronUpdateParam;
import com.guhecloud.cube.biz.daily.task.configuration.response.DailyCronResponse;
import com.guhecloud.cube.biz.daily.task.configuration.service.DailyCronService;
import com.guhecloud.cube.biz.daily.task.domain.po.DailyCronPO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * cron表达式
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DailyCronServiceImpl extends ServiceImpl<DailyCronMapper, DailyCronPO> implements DailyCronService {

    /**
     * 新增任务周期
     *
     * @param param 入参
     *
     * @return 主键ID
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long insertDailyCron(DailyCronInsertParam param) {
        var dailyCronPO = new DailyCronPO();
        dailyCronPO.setId(IdWorker.getId());
        dailyCronPO.setName(param.getName());
        dailyCronPO.setCron(param.getCron());
        dailyCronPO.setDescription(param.getDescription());
        dailyCronPO.setDailyType(param.getDailyType());
        this.save(dailyCronPO);
        return dailyCronPO.getId();
    }

    /**
     * 删除任务周期
     *
     * @return 是否成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteDailyCron(DailyCronBatchDeleteParam param) {
        return this.removeByIds(param.getIds());
    }

    /**
     * 修改任务周期
     *
     * @param param 入参
     *
     * @return 是否成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateDailyCron(DailyCronUpdateParam param) {
        var dailyCronPO = this.getById(param.getId());
        Assert.isNotFound(dailyCronPO, "未查询到相关信息");
        if (param.getName() != null) {
            dailyCronPO.setName(param.getName());
        }
        if (param.getCron() != null) {
            dailyCronPO.setCron(param.getCron());
        }
        if (param.getDescription() != null) {
            dailyCronPO.setDescription(param.getDescription());
        }
        if (param.getDailyType() != null) {
            dailyCronPO.setDailyType(param.getDailyType());
        }
        return this.updateById(dailyCronPO);
    }

    /**
     * 查询任务周期分页
     *
     * @param param 查询条件
     *
     * @return 分页列表
     */
    @Override
    public Pagination<DailyCronResponse> queryDailyCronPage(DailyCronPageParam param) {
        var page = this.page(
                PagingUtils.convert(param),
                Wrappers.<DailyCronPO>lambdaQuery()
                        .like(StringUtils.isNoneBlank(param.getName()), DailyCronPO::getName, param.getName())
                        .eq(StringUtils.isNoneBlank(param.getDailyType()), DailyCronPO::getDailyType, param.getDailyType())
                        .orderByDesc(DailyCronPO::getCreateTime)
                            );
        return PagingUtils.convert(page, DailyCronResponse.class);
    }
}
