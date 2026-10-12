package com.guhecloud.cube.srv.gen.safety.education.server.headquarters.exam.application.commandgateway.validator;

import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.exam.application.HeadquartersExamTaskService;
import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.exam.application.commandgateway.command.DeleteHeadquartersExamTaskCommand;
import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.exam.domain.po.HeadquartersExamTaskPO;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.enumeration.ExamTaskStatusEnum;
import com.guhecloud.platform.base.standard.exception.Assert;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DeleteHeadquartersExamTaskValidator {

    private final HeadquartersExamTaskService headquartersExamTaskService;

    public void verify(final DeleteHeadquartersExamTaskCommand deleteExamTaskCommand) {
        List<HeadquartersExamTaskPO> examTaskPOS = this.headquartersExamTaskService.listByIds(deleteExamTaskCommand.getExamTaskIds());
        examTaskPOS.forEach(examTaskPO -> {
            Assert.isNotAcceptable(ExamTaskStatusEnum.STAY_EXAM == examTaskPO.getExamTaskStatus(), "请撤回后再删除");
            Assert.isConflict(ExamTaskStatusEnum.IN_PROGRESS == examTaskPO.getExamTaskStatus(), "考试正在进行中");
        });
    }
}
