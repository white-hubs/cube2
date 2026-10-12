package com.guhecloud.cube.biz.daily.task.plan.controller;

import com.guhecloud.cube.biz.daily.task.commons.result.DailyPlanDetailResult;
import com.guhecloud.cube.biz.daily.task.param.DailyPlanCommandParam;
import com.guhecloud.cube.biz.daily.task.param.DailyPlanEnableParam;
import com.guhecloud.cube.biz.daily.task.param.DailyPlanPageParam;
import com.guhecloud.cube.biz.daily.task.plan.result.DailyPlanPageResult;
import com.guhecloud.cube.biz.daily.task.plan.service.DailyPlanService;
import com.guhecloud.platform.base.model.Pagination;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 计划
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DailyPlanController {

    private final DailyPlanService dailyPlanService;

    /**
     * 计划分页列表
     *
     * @param param 条件
     *
     * @return 分页
     */
    @GetMapping("/daily/plan/page")
    public Pagination<DailyPlanPageResult> pageDailyPlan(DailyPlanPageParam param) {
        return dailyPlanService.pageDailyPlan(param);
    }

    /**
     * 计划详情
     *
     * @param id 主键
     *
     * @return 详情
     */
    @GetMapping("/daily/plan/detail")
    public DailyPlanDetailResult detailDailyPlan(Long id) {
        return dailyPlanService.detailDailyPlan(id);
    }

    /**
     * 添加计划
     *
     * @param param 计划入参
     *
     * @return id
     */
    @PostMapping("/daily/plan/add")
    public Long addDailyPlan(@RequestBody @Valid DailyPlanCommandParam param) {
        return dailyPlanService.addDailyPlan(param);
    }

    /**
     * 编辑计划
     *
     * @param param 计划入参
     *
     * @return 是否成功
     */
    @PostMapping("/daily/plan/edit")
    public Long editDailyPlan(@RequestBody @Valid DailyPlanCommandParam param) {
        return dailyPlanService.editDailyPlan(param);
    }

    /**
     * 启用计划
     *
     * @param param 启用计划入参
     *
     * @return 是否成功
     */
    @PutMapping("/daily/plan/enable")
    public Boolean enablePlan(@RequestBody @Valid DailyPlanEnableParam param) {
        return dailyPlanService.enablePlan(param);
    }

    /**
     * 删除计划
     *
     * @param id 主键
     *
     * @return 是否成功
     */

    @DeleteMapping("/daily/plan/remove")
    public Boolean removeDailyPlan(Long id) {
        return dailyPlanService.removeDailyPlan(id);
    }
}
