package com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.factory;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.application.commandgateway.command.IncreaseExamPaperCommand;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.enumeration.ExamPaperSourceEnum;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.aggregates.ExamPaper;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.entity.ExamPaperInfo;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.entity.ExamPaperQuestionCategoryInfo;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.entity.ExamPaperQuestionInfo;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.valueobj.ExamQuestionType;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.valueobj.ThemeInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * 试卷构造工厂
 *
 * @author lanchun
 * @date 2023/4/18 17:46
 */
public class ExamPaperFactory {

    private ExamPaperFactory() {

    }

    public static ExamPaper newExamPaper(final IncreaseExamPaperCommand increaseExamPaperCommand) {
        //考卷基本信息
        ExamPaperInfo examPaperInfo = new ExamPaperInfo(increaseExamPaperCommand.getName(), increaseExamPaperCommand.getScore(), increaseExamPaperCommand.getPassingGradeScore(), increaseExamPaperCommand.getRemark());
        //试卷大题信息
        List<ExamPaperQuestionCategoryInfo> examPaperQuestionCategoryInfos = new ArrayList<>(increaseExamPaperCommand
                                                                                                     .getQuestionCategories().size());
        increaseExamPaperCommand.getQuestionCategories().forEach(increaseExamPaperQuestionCategoryCommand -> {
            //题目信息
            List<ExamPaperQuestionInfo> examPaperQuestionInfos = new ArrayList<>(increaseExamPaperQuestionCategoryCommand.getExamQuestions()
                                                                                         .size());
            increaseExamPaperQuestionCategoryCommand.getExamQuestions()
                    .forEach(increaseExamPaperQuestionCommand -> examPaperQuestionInfos.add(new ExamPaperQuestionInfo(IdWorker.getId(), increaseExamPaperQuestionCommand.getName(), new ThemeInfo(increaseExamPaperQuestionCommand.getThemeId(), null), new ExamQuestionType(increaseExamPaperQuestionCommand.getQuestionType(), increaseExamPaperQuestionCommand
                            .getQuestionType()
                            .getDescription()), increaseExamPaperQuestionCommand.getQuestionContent(), increaseExamPaperQuestionCommand.getScore(), increaseExamPaperQuestionCommand.getAnswerAnalysis(), increaseExamPaperQuestionCommand.getSort())));
            examPaperQuestionCategoryInfos.add(new ExamPaperQuestionCategoryInfo(IdWorker.getId(), increaseExamPaperQuestionCategoryCommand.getTitle(), increaseExamPaperQuestionCategoryCommand.getSort(), examPaperQuestionInfos));
        });
        ExamPaper examPaper = new ExamPaper(IdWorker.getId(), examPaperInfo, examPaperQuestionCategoryInfos);
        examPaper.setPaperSource(ExamPaperSourceEnum.MANUAL);
        return examPaper;
    }
}
