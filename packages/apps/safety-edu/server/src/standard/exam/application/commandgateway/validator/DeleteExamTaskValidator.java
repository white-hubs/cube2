package com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.application.commandgateway.validator;

import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.application.ExamTaskService;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.application.commandgateway.command.DeleteExamTaskCommand;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.enumeration.ExamTaskStatusEnum;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.po.ExamTaskPO;
import com.guhecloud.platform.base.standard.exception.Assert;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DeleteExamTaskValidator {

    private final ExamTaskService examTaskService;

    public void verify(final DeleteExamTaskCommand deleteExamTaskCommand) {
        List<ExamTaskPO> examTaskPOS = this.examTaskService.listByIds(deleteExamTaskCommand.getExamTaskIds());
        examTaskPOS.forEach(examTaskPO -> {
            Assert.isNotAcceptable(ExamTaskStatusEnum.STAY_EXAM == examTaskPO.getExamTaskStatus(), "请撤回后再删除");
            Assert.isNotAcceptable(ExamTaskStatusEnum.IN_PROGRESS == examTaskPO.getExamTaskStatus(), "考试正在进行中");
        });
    }
}
