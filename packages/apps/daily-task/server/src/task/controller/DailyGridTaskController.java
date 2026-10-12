package com.guhecloud.cube.biz.daily.task.task.controller;

import com.guhecloud.cube.biz.daily.task.commons.result.DailyTaskPageResult;
import com.guhecloud.cube.biz.daily.task.param.DailyTaskPageParam;
import com.guhecloud.cube.biz.daily.task.task.service.DailyTaskService;
import com.guhecloud.platform.base.model.Pagination;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 网格任务
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DailyGridTaskController {
    private final DailyTaskService dailyTaskService;

    /**
     * 网格任务分页查询
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/daily/task/page/grid")
    public Pagination<DailyTaskPageResult> gridPageDailyTask(@Valid DailyTaskPageParam param) {
        return dailyTaskService.pageDailyTask(param);
    }

}
