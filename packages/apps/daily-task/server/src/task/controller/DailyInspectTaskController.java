package com.guhecloud.cube.biz.daily.task.task.controller;

import com.guhecloud.cube.biz.daily.task.commons.result.DailyTaskPageResult;
import com.guhecloud.cube.biz.daily.task.param.DailyTaskPageParam;
import com.guhecloud.cube.biz.daily.task.task.service.DailyTaskService;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.security.permission.annotation.DataPermissionSwitch;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/12/3 16:23
 * @Description:
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DailyInspectTaskController {
    private final DailyTaskService dailyTaskService;

    /**
     * 日常巡检
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/daily/task/page/inspect")
    @DataPermissionSwitch(dataModel = "dailyTask:inspectTask")
    public Pagination<DailyTaskPageResult> inspectPageDailyTask(@Valid DailyTaskPageParam param) {
        return dailyTaskService.pageDailyTask(param);
    }

}
