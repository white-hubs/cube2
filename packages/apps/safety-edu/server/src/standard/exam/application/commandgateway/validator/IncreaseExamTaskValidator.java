package com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.application.commandgateway.validator;

import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.application.commandgateway.command.IncreaseExamTaskCommand;
import com.guhecloud.cube.srv.gen.safety.education.server.common.application.querygateway.ExamQuestionRuleQueryService;
import com.guhecloud.cube.srv.gen.safety.education.server.common.interfaces.vo.ExamQuestionRuleVO;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.application.querygateway.ExamPaperQueryService;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.enumeration.ExamTaskPaperModeEnum;
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
public class IncreaseExamTaskValidator {

    private final ExamPaperQueryService examPaperQueryService;

    private final ExamQuestionRuleQueryService examQuestionRuleQueryService;

    public void verify(final IncreaseExamTaskCommand increaseExamTaskCommand) {
        if (ExamTaskPaperModeEnum.UNIFIED == increaseExamTaskCommand.getExamPaperMode()) {
            List<ExamPaperVO> examPapersIfExist = this.examPaperQueryService.getExamPapersIfExist(increaseExamTaskCommand.getExamPaperIds());
            Set<Float> scoreSize = examPapersIfExist.stream()
                    .map(ExamPaperVO::getScore)
                    .collect(Collectors.toSet());
            Assert.isNotAcceptable(scoreSize.size() > 1, "试卷总分不一致");
            return;
        }
        List<ExamQuestionRuleVO> rules = this.examQuestionRuleQueryService.listExamQuestionRulesByIds(increaseExamTaskCommand.getExamPaperIds());
        Assert.isNotAcceptable(rules.size() != increaseExamTaskCommand.getExamPaperIds().size(), "存在无效的考题规则");
        Set<Integer> totals = rules.stream().map(ExamQuestionRuleVO::getTotalScore).collect(Collectors.toSet());
        Assert.isNotAcceptable(totals.size() > 1, "试卷总分不一致");
    }
}
