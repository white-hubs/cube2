package com.guhecloud.cube.biz.daily.task.task.controller;

import com.guhecloud.cube.biz.daily.task.commons.dto.DailyStandardResultDTO;
import com.guhecloud.cube.biz.daily.task.commons.dto.DailyTargetDTO;
import com.guhecloud.cube.biz.daily.task.commons.result.DailyPlanTargetResult;
import com.guhecloud.cube.biz.daily.task.commons.result.DailyTaskDetailResult;
import com.guhecloud.cube.biz.daily.task.commons.result.DailyTaskHomePageResult;
import com.guhecloud.cube.biz.daily.task.commons.result.DailyTaskPageResult;
import com.guhecloud.cube.biz.daily.task.commons.result.DailyTaskSummaryResult;
import com.guhecloud.cube.biz.daily.task.commons.result.DailyTaskTargetResult;
import com.guhecloud.cube.biz.daily.task.param.DailyBaseStatisticsParam;
import com.guhecloud.cube.biz.daily.task.param.DailyTaskAddParam;
import com.guhecloud.cube.biz.daily.task.param.DailyTaskAssignParam;
import com.guhecloud.cube.biz.daily.task.param.DailyTaskFeedbackParam;
import com.guhecloud.cube.biz.daily.task.param.DailyTaskOperationParam;
import com.guhecloud.cube.biz.daily.task.param.DailyTaskOtherTargetParam;
import com.guhecloud.cube.biz.daily.task.param.DailyTaskPageParam;
import com.guhecloud.cube.biz.daily.task.param.DailyTaskSubmitParam;
import com.guhecloud.cube.biz.daily.task.param.DailyTaskTargetQueryParam;
import com.guhecloud.cube.biz.daily.task.task.service.DailyTaskService;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.security.permission.annotation.DataPermissionSwitch;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 日常任务操作
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DailyTaskController {

    private final DailyTaskService dailyTaskService;

    /**
     * 分页列表
     *
     * @param param 条件
     *
     * @return 分页列表
     */
    @GetMapping("/daily/task/page")
    @DataPermissionSwitch(dataModel = "dailyTask:task")
    public Pagination<DailyTaskPageResult> pageDailyTask(@Valid DailyTaskPageParam param) {
        return dailyTaskService.pageDailyTask(param);
    }

    /**
     * 批量查询网格异常任务数
     */
    @PostMapping("/daily/task/grid/abnormal-count")
    public Map<Long, Long> getGridAbnormalTaskCount(@RequestBody List<Long> gridAreaIds,
                                                    @RequestParam("dailyType") String dailyType) {
        return dailyTaskService.getGridAbnormalTaskCount(gridAreaIds, dailyType);
    }

    /**
     * 任务详情
     */
    @GetMapping("/daily/task/detail")
    public DailyTaskDetailResult detailDailyTask(@RequestParam("id") Long id) {
        return dailyTaskService.detailDailyTask(id);
    }

    /**
     * 根据任务id获取任务关联对象信息
     */
    @GetMapping("/daily/task/relate")
    public Pagination<DailyTaskTargetResult> relateDailyTask(@Valid DailyTaskTargetQueryParam param) {
        return dailyTaskService.relateDailyTask(param);
    }

    /**
     * 添加任务
     *
     * @param param 入参
     *
     * @return 主键
     */
    @PostMapping("/daily/task/add")
    public Long addDailyTask(@RequestBody @Valid DailyTaskAddParam param) {
        return dailyTaskService.createDailyTaskBatch(Collections.singletonList(param)).getFirst();
    }

    /**
     * 草稿箱编辑
     */
    @PostMapping("/daily/task/edit")
    public Boolean editDailyTask(@RequestBody @Valid DailyTaskAddParam param) {
        return dailyTaskService.editDailyTask(param);
    }

    /**
     * 删除任务
     */
    @DeleteMapping("/daily/task/delete/{id}")
    public Boolean deleteDailyTask(@PathVariable("id") Long id) {
        return dailyTaskService.deleteDailyTask(id, null, null);
    }

    /**
     * 移交
     */
    @PostMapping("/daily/task/transfer")
    public Boolean transferDailyTask(@RequestBody DailyTaskOperationParam param) {
        return dailyTaskService.transferDailyTask(param);
    }

    /**
     * 分发
     */
    @PostMapping("/daily/task/distribute")
    public Boolean distributeDailyTask(@RequestBody DailyTaskOperationParam param) {
        return dailyTaskService.distributeDailyTask(param);
    }

    /**
     * 指派（百分比方式）
     */
    @PostMapping("/daily/task/assign")
    public Boolean assignDailyTask(@RequestBody DailyTaskAssignParam assignParam) {
        return dailyTaskService.assignDailyTask(assignParam);
    }

    /**
     * 任务提交
     */
    @PostMapping("/daily/task/submit")
    public Boolean submitDailyTask(@RequestBody DailyTaskSubmitParam param) {
        return dailyTaskService.submitDailyTask(param);
    }

    /**
     * 任务终止
     */
    @PostMapping("/daily/task/terminate/{id}")
    public Boolean terminateDailyTask(@PathVariable("id") Long id) {
        return dailyTaskService.terminateDailyTask(id);
    }

    /**
     * 根据对象获取任务项
     */
    @GetMapping("/daily/task/target")
    public List<DailyStandardResultDTO> getDailyTaskByTarget(@RequestParam("taskId") Long taskId, @RequestParam("id") Long id) {
        return dailyTaskService.getDailyTaskByTarget(taskId, id);
    }

    /**
     * 根据任务id获取任务关联对象信息（APP使用）
     */
    @GetMapping("/daily/task/relate/app")
    public Map<String, List<DailyPlanTargetResult>> relateDailyTaskApp(DailyTaskTargetQueryParam param) {
        return dailyTaskService.relateDailyTaskApp(param);
    }

    /**
     * 根据任务id获取任务关联对象信息（PC使用）
     */
    @GetMapping("/daily/task/relate/pc")
    public List<DailyPlanTargetResult> relateDailyTaskPc(DailyTaskTargetQueryParam param) {
        return dailyTaskService.relateDailyTaskPc(param);
    }

    /**
     * 首页面板统计
     */
    @GetMapping("/daily/task/statistics")
    @DataPermissionSwitch(dataModel = "dailyTask:task")
    public DailyTaskHomePageResult statisticsDailyTask(DailyBaseStatisticsParam param) {
        return dailyTaskService.statisticsDailyTask(param);
    }

    /**
     * 任务执行进度汇总
     */
    @GetMapping("/daily/task/summary")
    public DailyTaskSummaryResult getDailyTaskSummary(@RequestParam Long taskId) {
        return dailyTaskService.getDailyTaskSummary(taskId);

    }

    /**
     * 获取任务下该档位 档主的其他[执行内容相同的]档位
     * 注意不包含自己
     *
     * @param param 条件
     *
     * @return 对象(档位)列表
     */
    @PostMapping("/daily/task/other/target")
    public List<DailyTargetDTO> getTaskOtherTarget(@RequestBody DailyTaskOtherTargetParam param) {
        return dailyTaskService.getTaskOtherTarget(param);
    }

    /**
     * 获取当前任务下存在的标签
     */
    @GetMapping("/daily/task/tags")
    public List<Long> getDailyTaskTargetIdList(@RequestParam Long taskId) {
        return dailyTaskService.getDailyTaskTargetIdList(taskId);
    }

    /**
     * 获取当前任务还能分发的百分比数
     */
    @GetMapping("/daily/task/percent")
    public BigDecimal getDailyTaskPercent(@RequestParam Long taskId) {
        return dailyTaskService.getDailyTaskPercent(taskId);
    }

    /**
     * 根据任务id和对象id获取任务关联对象信息
     */
    @GetMapping("/daily/task/target/relate")
    public DailyTaskTargetResult getDailyTaskTargetRelate(@RequestParam("taskId") Long taskId, @RequestParam("id") Long id) {
        return dailyTaskService.getDailyTaskTargetRelate(taskId, id);
    }

    /**
     * 任务反馈
     */
    @PostMapping("/daily/task/feedback")
    public Boolean feedbackDailyTask(@RequestBody @Valid DailyTaskFeedbackParam param) {
        return dailyTaskService.feedbackDailyTask(param);
    }
}
