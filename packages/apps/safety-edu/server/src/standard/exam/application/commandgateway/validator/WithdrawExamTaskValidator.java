package com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.application.commandgateway.validator;

import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.application.ExamTaskService;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.application.commandgateway.command.WithdrawExamTaskCommand;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.enumeration.ExamTaskStatusEnum;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.po.ExamTaskPO;
import com.guhecloud.platform.base.standard.exception.Assert;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 任务撤回校验器
 *
 * @author lanchun
 * @date 2023/4/19 19:29
 */
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WithdrawExamTaskValidator {

    private final ExamTaskService examTaskService;

    public void verify(final WithdrawExamTaskCommand withdrawExamTaskCommand) {
        ExamTaskPO examTaskPO = this.examTaskService.getById(withdrawExamTaskCommand.getId());
        Assert.isNotAcceptable(ExamTaskStatusEnum.UNPUBLISHED == examTaskPO.getExamTaskStatus(), "考试任务未发布");
        Assert.isNotAcceptable(ExamTaskStatusEnum.IN_PROGRESS == examTaskPO.getExamTaskStatus(), "考试正在进行中");
        Assert.isNotAcceptable((ExamTaskStatusEnum.ENDED == examTaskPO.getExamTaskStatus()), "考试已结束");
    }
}
