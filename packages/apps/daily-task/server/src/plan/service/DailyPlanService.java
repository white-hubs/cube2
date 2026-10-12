package com.guhecloud.cube.biz.daily.task.plan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.daily.task.commons.result.DailyPlanDetailResult;
import com.guhecloud.cube.biz.daily.task.domain.po.DailyPlanPO;
import com.guhecloud.cube.biz.daily.task.param.DailyPlanCommandParam;
import com.guhecloud.cube.biz.daily.task.param.DailyPlanEnableParam;
import com.guhecloud.cube.biz.daily.task.param.DailyPlanPageParam;
import com.guhecloud.cube.biz.daily.task.plan.dto.DailyPlanGenerateTaskAfterUpdateDTO;
import com.guhecloud.cube.biz.daily.task.plan.result.DailyPlanPageResult;
import com.guhecloud.platform.base.model.Pagination;

import java.util.List;

/**
 * 计划
 */
public interface DailyPlanService extends IService<DailyPlanPO> {
    Pagination<DailyPlanPageResult> pageDailyPlan(DailyPlanPageParam param);

    DailyPlanDetailResult detailDailyPlan(Long id);

    Long addDailyPlan(DailyPlanCommandParam param);

    Long editDailyPlan(DailyPlanCommandParam param);

    Boolean enablePlan(DailyPlanEnableParam param);

    Boolean removeDailyPlan(Long id);

    void batchUpdateMultiFields(List<DailyPlanGenerateTaskAfterUpdateDTO> updates);
}
