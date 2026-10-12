package com.guhecloud.cube.biz.daily.task.task.controller;

import com.guhecloud.cube.biz.daily.task.commons.result.DailyTaskFeedbackResult;
import com.guhecloud.cube.biz.daily.task.task.service.DailyTaskFeedbackService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 任务反馈
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DailyTaskFeedbackController {

    private final DailyTaskFeedbackService dailyTaskFeedbackService;

    /**
     * 根据任务id查询反馈列表
     */
    @GetMapping("/daily/task/feedback/list")
    public List<DailyTaskFeedbackResult> getFeedbackListByTaskId(@RequestParam("taskId") Long taskId) {
        return dailyTaskFeedbackService.getFeedbackListByTaskId(taskId);
    }
}
