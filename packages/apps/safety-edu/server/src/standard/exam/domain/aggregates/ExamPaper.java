package com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.aggregates;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.enumeration.ExamPaperSourceEnum;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.application.commandgateway.command.ChangeExamPaperQuestionCategoryCommand;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.entity.ExamPaperInfo;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.entity.ExamPaperQuestionCategoryInfo;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.entity.ExamPaperQuestionInfo;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.valueobj.ExamQuestionType;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.valueobj.ThemeInfo;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * 试卷聚合
 *
 * @author lanchun
 * @date 2023/4/18 16:48
 */
@Data
@NoArgsConstructor
public class ExamPaper {

    private Long id;

    /**
     * 试卷基础信息
     */
    private ExamPaperInfo examPaperInfo;

    /**
     * 试卷题目集合
     */
    private List<ExamPaperQuestionCategoryInfo> examPaperQuestionCategoryInfos;

    /**
     * 试卷来源
     */
    private ExamPaperSourceEnum paperSource;

    public ExamPaper(Long id, ExamPaperInfo examPaperInfo, List<ExamPaperQuestionCategoryInfo> examPaperQuestionCategoryInfos) {
        this.id = id;
        this.examPaperInfo = examPaperInfo;
        this.examPaperQuestionCategoryInfos = examPaperQuestionCategoryInfos;
    }

    public void changeExamPaperInfo(String name, Float score, Float passingGradeScore, String remark) {
        this.examPaperInfo = new ExamPaperInfo(name, score, passingGradeScore, remark);
    }

    /**
     * 设置试卷题目
     *
     * @param changeExamPaperQuestionCategoryCommands
     */
    public void settingExamPaperQuestionCategoryInfos(List<ChangeExamPaperQuestionCategoryCommand> changeExamPaperQuestionCategoryCommands
                                                     ) {
        //试卷大题信息
        List<ExamPaperQuestionCategoryInfo> localExamPaperQuestionCategoryInfos = new ArrayList<>(changeExamPaperQuestionCategoryCommands.size());
        changeExamPaperQuestionCategoryCommands.forEach(changeExamPaperQuestionCategoryCommand -> {
            //题目信息
            List<ExamPaperQuestionInfo> examPaperQuestionInfos = new ArrayList<>(changeExamPaperQuestionCategoryCommand.getExamQuestions()
                                                                                         .size());
            changeExamPaperQuestionCategoryCommand.getExamQuestions()
                    .forEach(changeExamPaperQuestionCommand ->
                                     examPaperQuestionInfos.add(new ExamPaperQuestionInfo(
                                             IdWorker.getId(),
                                             changeExamPaperQuestionCommand.getName(),
                                             new ThemeInfo(changeExamPaperQuestionCommand.getThemeId(), null),
                                             new ExamQuestionType(changeExamPaperQuestionCommand.getQuestionType(), changeExamPaperQuestionCommand
                                                     .getQuestionType().getDescription()),
                                             changeExamPaperQuestionCommand.getQuestionContent(),
                                             changeExamPaperQuestionCommand.getScore(),
                                             changeExamPaperQuestionCommand.getAnswerAnalysis(),
                                             changeExamPaperQuestionCommand.getSort()))
                            );
            localExamPaperQuestionCategoryInfos.add(new ExamPaperQuestionCategoryInfo(
                    IdWorker.getId(),
                    changeExamPaperQuestionCategoryCommand.getTitle(),
                    changeExamPaperQuestionCategoryCommand.getSort(),
                    examPaperQuestionInfos
            ));
        });
        this.examPaperQuestionCategoryInfos = localExamPaperQuestionCategoryInfos;
    }

}
