package com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.infrastructure.utils;

import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.entity.ExamTaskPaper;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.interfaces.vo.ExamPaperVO;
import com.guhecloud.platform.base.util.JsonUtils;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public final class ExamTaskPaperConvertUtils {

    private ExamTaskPaperConvertUtils() {
    }

    public static List<ExamTaskPaper> examPaperVOConvertExamTaskPaper(List<ExamPaperVO> examPaperVOS) {
        return examPaperVOS
                .stream()
                .map(examPaperVO -> {
                    ExamTaskPaper examTaskPaper = new ExamTaskPaper(examPaperVO.getId(), examPaperVO.getName(), JsonUtils.serialize(examPaperVO));
                    examTaskPaper.setRuleId(examPaperVO.getRuleId());
                    examTaskPaper.setUserIdentify(null);
                    examTaskPaper.setExamPaperScore(examPaperVO.getScore());
                    examTaskPaper.setGradeScore(examPaperVO.getPassingGradeScore());
                    examTaskPaper.setRequestNumber(examPaperVO.getExamPaperQuestionNumber());
                    examTaskPaper.setExamPaperRemark(examPaperVO.getRemark());
                    return examTaskPaper;
                }).collect(Collectors.toList());
    }
}
