package com.guhecloud.cube.biz.daily.task.configuration.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.daily.task.domain.po.DailyUrgencyPO;
import com.guhecloud.cube.biz.daily.task.configuration.param.DailyUrgencyInsertParam;
import com.guhecloud.cube.biz.daily.task.configuration.param.DailyUrgencyPageParam;
import com.guhecloud.cube.biz.daily.task.configuration.param.DailyUrgencyUpdateParam;
import com.guhecloud.cube.biz.daily.task.configuration.response.DailyUrgencyResponse;
import com.guhecloud.platform.base.model.Pagination;

import java.util.Map;
import java.util.Set;

public interface DailyUrgencyService extends IService<DailyUrgencyPO> {
    /**
     * 新增紧急程度
     *
     * @param param 入参
     *
     * @return 主键ID
     */
    Long insertDailyUrgency(DailyUrgencyInsertParam param);

    /**
     * 删除紧急程度
     *
     * @param id 主键ID
     *
     * @return 是否成功
     */
    Boolean deleteDailyUrgency(Long id);

    /**
     * 修改紧急程度
     *
     * @param param 入参
     *
     * @return 是否成功
     */
    Boolean updateDailyUrgency(DailyUrgencyUpdateParam param);

    /**
     * 查询紧急程度分页
     *
     * @param param 查询条件
     *
     * @return 分页列表
     */
    Pagination<DailyUrgencyResponse> queryDailyUrgencyPage(DailyUrgencyPageParam param);

    Map<Long, DailyUrgencyPO> getUrgencyByIds(Set<Long> urgencyIds);
}
