package com.guhecloud.cube.srv.gen.safety.education.server.headquarters.exam.application.commandgateway.validator;

import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.exam.application.HeadquartersExamTaskService;
import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.exam.application.commandgateway.command.ChangeHeadquartersExamTaskCommand;
import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.exam.domain.po.HeadquartersExamTaskPO;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.application.querygateway.ExamPaperQueryService;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.enumeration.ExamTaskStatusEnum;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.interfaces.vo.ExamPaperVO;
import com.guhecloud.platform.base.standard.exception.Assert;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 任务编辑校验
 *
 * @author lanchun
 * @date 2024/4/9 18:07
 */
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ChangeHeadquartersExamTaskValidator {

    private final HeadquartersExamTaskService headquartersExamTaskService;

    private final ExamPaperQueryService examPaperQueryService;

    public void verify(final ChangeHeadquartersExamTaskCommand changeExamTaskCommand) {
        HeadquartersExamTaskPO headquartersExamTaskPO = this.headquartersExamTaskService.getById(changeExamTaskCommand.getId());
        Assert.isNotFound(headquartersExamTaskPO, "考试任务不存在");
        Assert.isNotAcceptable(ExamTaskStatusEnum.STAY_EXAM == headquartersExamTaskPO.getExamTaskStatus(), "请撤回后再编辑");
        Assert.isConflict(ExamTaskStatusEnum.IN_PROGRESS == headquartersExamTaskPO.getExamTaskStatus(), "考试正在进行中");
        Assert.isNotAcceptable(ExamTaskStatusEnum.ENDED == headquartersExamTaskPO.getExamTaskStatus(), "考试已结束");
        List<ExamPaperVO> examPapersIfExist = this.examPaperQueryService.getExamPapersIfExist(changeExamTaskCommand.getExamPaperIds());
        Set<Float> scoreSize = examPapersIfExist.stream()
                .map(ExamPaperVO::getScore)
                .collect(Collectors.toSet());
        Assert.isConflict(scoreSize.size() > 1, "试卷总分不一致");
    }
}
