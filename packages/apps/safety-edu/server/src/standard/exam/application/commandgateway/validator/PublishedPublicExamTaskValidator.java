package com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.application.commandgateway.validator;

import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.application.ExamTaskService;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.application.commandgateway.command.PublishedExamTaskCommand;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.enumeration.ExamTaskStatusEnum;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.po.ExamTaskPO;
import com.guhecloud.platform.base.standard.exception.Assert;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 任务发布校验器
 *
 * @author lanchun
 * @date 2023/4/19 19:29
 */
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PublishedPublicExamTaskValidator {

    private final ExamTaskService examTaskService;

    public void verify(final PublishedExamTaskCommand publishedExamTaskValidator) {
        ExamTaskPO examTaskPO = this.examTaskService.getById(publishedExamTaskValidator.getId());
        Assert.isOutOfRange(examTaskPO.getExamEndTime().isBefore(LocalDateTime.now()), "发布失败,考试结束已超过当前时间");
        Assert.isConflict(ExamTaskStatusEnum.STAY_EXAM == examTaskPO.getExamTaskStatus(), "任务已发布");
        Assert.isNotAcceptable(ExamTaskStatusEnum.IN_PROGRESS == examTaskPO.getExamTaskStatus(), "考试正在进行中");
        Assert.isNotAcceptable(ExamTaskStatusEnum.ENDED == examTaskPO.getExamTaskStatus(), "考试已结束");
    }
}
