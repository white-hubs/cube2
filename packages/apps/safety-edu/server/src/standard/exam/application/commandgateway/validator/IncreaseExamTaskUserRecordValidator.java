package com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.application.commandgateway.validator;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.application.ExamTaskService;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.application.ExamTaskUserService;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.application.commandgateway.command.IncreaseExamTaskUserRecordCommand;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.po.ExamTaskPO;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.po.ExamTaskUserPO;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class IncreaseExamTaskUserRecordValidator {

    private final ExamTaskUserService examTaskUserService;

    private final ExamTaskService examTaskService;

    public void verify(final IncreaseExamTaskUserRecordCommand increaseExamTaskUserRecordCommand) {
        ExamTaskUserPO examTaskUserPO = this.examTaskUserService.getOne(Wrappers.<ExamTaskUserPO>lambdaQuery()
                                                                                .eq(ExamTaskUserPO::getExamTaskId, increaseExamTaskUserRecordCommand.getExamTaskId())
                                                                                .eq(ExamTaskUserPO::getUserId, SecurityContextHolder
                                                                                        .getPrincipal().getId()));
        Assert.isNotFound(examTaskUserPO, "提交失败,当前用户不再此任务中");
        ExamTaskPO examTaskPO = this.examTaskService.getById(increaseExamTaskUserRecordCommand.getExamTaskId());
        if (examTaskUserPO.getExamNumber() > 0) {
            Assert.isOutOfRange(!examTaskPO.getRetake(), "提交失败,已超过考试次数");
            Assert.isOutOfRange(examTaskPO.getRetake() &&
                                examTaskPO.getRetakeNumber() <= examTaskUserPO.getExamNumber() - 1, "提交失败,已超过考试次数");
        }
    }
}
