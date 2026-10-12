package com.guhecloud.cube.srv.gen.safety.education.server.headquarters.exam.application.commandgateway.validator;

import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.exam.application.HeadquartersExamTaskService;
import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.exam.application.commandgateway.command.WithdrawHeadquartersExamTaskCommand;
import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.exam.domain.po.HeadquartersExamTaskPO;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.enumeration.ExamTaskStatusEnum;
import com.guhecloud.platform.base.standard.exception.Assert;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author lanchun
 * @date 2024/4/9 18:11
 */
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WithdrawHeadquartersExamTaskValidator {

    private final HeadquartersExamTaskService headquartersExamTaskService;

    public void verify(final WithdrawHeadquartersExamTaskCommand withdrawExamTaskCommand) {
        HeadquartersExamTaskPO examTaskPO = this.headquartersExamTaskService.getById(withdrawExamTaskCommand.getId());
        Assert.isNotAcceptable(ExamTaskStatusEnum.UNPUBLISHED == examTaskPO.getExamTaskStatus(), "考试任务未发布");
        Assert.isNotAcceptable(ExamTaskStatusEnum.IN_PROGRESS == examTaskPO.getExamTaskStatus(), "考试正在进行中");
        Assert.isNotAcceptable(ExamTaskStatusEnum.ENDED == examTaskPO.getExamTaskStatus(), "考试已结束");
    }
}
