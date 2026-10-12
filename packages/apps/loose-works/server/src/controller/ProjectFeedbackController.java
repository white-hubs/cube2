package com.guhecloud.cube.srv.gen.loose.works.controller;

import com.guhecloud.cube.srv.gen.loose.works.dto.vo.ProjectDetailVO;
import com.guhecloud.cube.srv.gen.loose.works.dto.vo.ProjectFeedbackVO;
import com.guhecloud.cube.srv.gen.loose.works.service.ProjectFeedbackService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 工程反馈管理
 *
 * @Author: lijie
 * @CreateTime: 2024-11-11  13:56
 * @Description:
 * @Version: 1.0
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ProjectFeedbackController {
    private final ProjectFeedbackService projectFeedbackService;

    /**
     * 工程反馈、验收详情
     */
    @GetMapping("/project/feedback/get/by/id")
    public ProjectFeedbackVO projectScheduleGetById(@RequestParam("id") Long id) {
        return projectFeedbackService.getFeedBackById(id);
    }

    /**
     * 工程反馈、验收信息
     */
    @GetMapping("/project/feedback/by/schedule/id")
    public List<ProjectDetailVO.FeedbackDetail> projectScheduleByScheduleId(@RequestParam("scheduleId") Long scheduleId,
                                                                            @RequestParam("feedbackType") String feedbackType
                                                                           ) {
        return projectFeedbackService.getFeedbackDetailAll(scheduleId, feedbackType);
    }

}
