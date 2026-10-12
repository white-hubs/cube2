package com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.application.commandgateway.validator;

import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.application.commandgateway.command.ChangeExamPaperCommand;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.application.querygateway.ExamTaskQueryService;
import com.guhecloud.platform.base.standard.exception.Assert;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ChangeExamPaperValidator {

    private final ExamTaskQueryService examTaskQueryService;

    public void verify(final ChangeExamPaperCommand changeExamPaperCommand) {
        //校验试卷是否在进行中或待考试的考试任务中,若再则不允许编辑
        int examTaskCount = examTaskQueryService.getUsedExamTaskByPaperId(changeExamPaperCommand.getId());
        Assert.isNotAcceptable(examTaskCount > 0, "试卷正在使用中");
    }
}
