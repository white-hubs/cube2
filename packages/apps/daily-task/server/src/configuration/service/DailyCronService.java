package com.guhecloud.cube.biz.daily.task.configuration.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.daily.task.configuration.param.DailyCronBatchDeleteParam;
import com.guhecloud.cube.biz.daily.task.configuration.param.DailyCronInsertParam;
import com.guhecloud.cube.biz.daily.task.configuration.param.DailyCronPageParam;
import com.guhecloud.cube.biz.daily.task.configuration.param.DailyCronUpdateParam;
import com.guhecloud.cube.biz.daily.task.configuration.response.DailyCronResponse;
import com.guhecloud.cube.biz.daily.task.domain.po.DailyCronPO;
import com.guhecloud.platform.base.model.Pagination;

public interface DailyCronService extends IService<DailyCronPO> {
    /**
     * 新增任务周期
     *
     * @param param 入参
     *
     * @return 主键ID
     */
    Long insertDailyCron(DailyCronInsertParam param);

    /**
     * 删除任务周期
     *
     * @return 是否成功
     */
    Boolean deleteDailyCron(DailyCronBatchDeleteParam param);

    /**
     * 修改任务周期
     *
     * @param param 入参
     *
     * @return 是否成功
     */
    Boolean updateDailyCron(DailyCronUpdateParam param);

    /**
     * 查询任务周期分页
     *
     * @param param 查询条件
     *
     * @return 分页列表
     */
    Pagination<DailyCronResponse> queryDailyCronPage(DailyCronPageParam param);
}
