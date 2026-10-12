package com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.application.commandgateway.validator;

import com.guhecloud.cube.srv.gen.safety.education.server.common.application.querygateway.ExamQuestionRuleQueryService;
import com.guhecloud.cube.srv.gen.safety.education.server.common.interfaces.vo.ExamQuestionRuleVO;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.application.ExamTaskService;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.application.commandgateway.command.ChangeExamTaskCommand;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.application.querygateway.ExamPaperQueryService;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.enumeration.ExamTaskPaperModeEnum;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.enumeration.ExamTaskStatusEnum;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.po.ExamTaskPO;
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
 * @date 2023/4/19 17:03
 */
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ChangeExamTaskValidator {

    private final ExamTaskService examTaskService;

    private final ExamPaperQueryService examPaperQueryService;

    private final ExamQuestionRuleQueryService examQuestionRuleQueryService;

    public void verify(final ChangeExamTaskCommand changeExamTaskCommand) {
        ExamTaskPO examTaskPO = this.examTaskService.getById(changeExamTaskCommand.getId());
        Assert.isNotFound(examTaskPO, "考试任务不存在");
        Assert.isNotAcceptable(ExamTaskStatusEnum.STAY_EXAM == examTaskPO.getExamTaskStatus(), "请撤回后再编辑");
        Assert.isNotAcceptable(ExamTaskStatusEnum.IN_PROGRESS == examTaskPO.getExamTaskStatus(), "考试正在进行中");
        Assert.isNotAcceptable(ExamTaskStatusEnum.ENDED == examTaskPO.getExamTaskStatus(), "考试已结束");
        if (ExamTaskPaperModeEnum.UNIFIED == changeExamTaskCommand.getExamPaperMode()) {
            List<ExamPaperVO> examPapersIfExist = this.examPaperQueryService.getExamPapersIfExist(changeExamTaskCommand.getExamPaperIds());
            Set<Float> scoreSize = examPapersIfExist.stream()
                    .map(ExamPaperVO::getScore)
                    .collect(Collectors.toSet());
            Assert.isNotAcceptable(scoreSize.size() > 1, "试卷总分不一致");
            return;
        }
        List<ExamQuestionRuleVO> rules = this.examQuestionRuleQueryService.listExamQuestionRulesByIds(changeExamTaskCommand.getExamPaperIds());
        Assert.isNotAcceptable(rules.size() != changeExamTaskCommand.getExamPaperIds().size(), "存在无效的考题规则");
        Set<Integer> totals = rules.stream().map(ExamQuestionRuleVO::getTotalScore).collect(Collectors.toSet());
        Assert.isNotAcceptable(totals.size() > 1, "试卷总分不一致");
    }
}
