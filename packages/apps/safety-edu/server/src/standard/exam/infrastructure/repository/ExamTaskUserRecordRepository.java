package com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.infrastructure.repository;

import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.aggregates.UserExamTaskRecord;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.entity.ExamPaperQuestionCategoryInfo;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.entity.ExamPaperQuestionInfo;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.po.ExamTaskUserRecordPO;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.infrastructure.repository.mapper.ExamTaskUserRecordMapper;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.interfaces.vo.ExamPaperQuestionCategoryVO;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.interfaces.vo.ExamPaperQuestionVO;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.interfaces.vo.ExamPaperVO;
import com.guhecloud.platform.base.util.JsonUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ExamTaskUserRecordRepository {

    private final ExamTaskUserRecordMapper examTaskUserRecordMapper;

    @Transactional(rollbackFor = Exception.class)
    public UserExamTaskRecord save(UserExamTaskRecord userExamTaskRecord) {
        Integer examPaperQuestionNumber = userExamTaskRecord.getExamPaper().getExamPaperQuestionCategoryInfos()
                .stream()
                .mapToInt(examPaperQuestionCategoryInfos -> examPaperQuestionCategoryInfos.getExamPaperQuestionInfos().size())
                .sum();
        //获取试卷大题
        List<ExamPaperQuestionCategoryInfo> examPaperQuestionCategoryInfos = userExamTaskRecord.getExamPaper()
                .getExamPaperQuestionCategoryInfos();
        List<ExamPaperQuestionCategoryVO> examPaperQuestionCategoryVOS = new ArrayList<>(examPaperQuestionCategoryInfos.size());
        //试卷题目
        List<ExamPaperQuestionVO> userExamTaskPaperQuestionVOS = new ArrayList<>();
        examPaperQuestionCategoryInfos.stream().forEach(examPaperQuestionCategoryInfo -> {
            List<ExamPaperQuestionInfo> examPaperQuestionInfos = examPaperQuestionCategoryInfo.getExamPaperQuestionInfos();
            List<ExamPaperQuestionVO> examPaperQuestionVOS = new ArrayList<>(examPaperQuestionInfos.size());
            examPaperQuestionInfos.stream().forEach(examPaperQuestionPO -> {
                ExamPaperQuestionVO examPaperQuestionVO = ExamPaperQuestionVO.builder()
                        .id(examPaperQuestionPO.getId())
                        .name(examPaperQuestionPO.getName())
                        .examPaperQuestionCategoryId(examPaperQuestionCategoryInfo.getId())
                        .examPaperQuestionCategoryTitle(examPaperQuestionCategoryInfo.getTitle())
                        .themeId(examPaperQuestionPO.getThemeInfo().getId())
                        .themeName(examPaperQuestionPO.getThemeInfo().getName())
                        .questionType(examPaperQuestionPO.getQuestionType().getQuestionType())
                        .questionTypeName(examPaperQuestionPO.getQuestionType().getQuestionType().getDescription())
                        .questionContent(examPaperQuestionPO.getQuestionContent())
                        .score(examPaperQuestionPO.getScore())
                        .answerAnalysis(examPaperQuestionPO.getAnswerAnalysis())
                        .sort(examPaperQuestionPO.getSort())
                        .answerState(examPaperQuestionPO.getAnswerState())
                        .editState(examPaperQuestionPO.getEditState())
                        .build();
                examPaperQuestionVOS.add(examPaperQuestionVO);
                userExamTaskPaperQuestionVOS.add(examPaperQuestionVO);
            });
            ExamPaperQuestionCategoryVO examPaperQuestionCategoryVO = ExamPaperQuestionCategoryVO.builder()
                    .id(examPaperQuestionCategoryInfo.getId())
                    .title(examPaperQuestionCategoryInfo.getTitle())
                    .sort(examPaperQuestionCategoryInfo.getSort())
                    .examQuestions(examPaperQuestionVOS)
                    .build();
            //大题下的题目
            examPaperQuestionCategoryVO.setExamQuestions(examPaperQuestionVOS);
            examPaperQuestionCategoryVOS.add(examPaperQuestionCategoryVO);
        });
        //试卷内容
        ExamPaperVO examPaperVO = ExamPaperVO.builder()
                .id(userExamTaskRecord.getExamPaper().getId())
                .name(userExamTaskRecord.getExamPaper().getExamPaperInfo().getName())
                .remark(userExamTaskRecord.getExamPaper().getExamPaperInfo().getRemark())
                .score(userExamTaskRecord.getExamPaper().getExamPaperInfo().getScore())
                .passingGradeScore(userExamTaskRecord.getExamPaper().getExamPaperInfo().getPassingGradeScore())
                .examPaperQuestionNumber(examPaperQuestionNumber)
                .questionCategories(examPaperQuestionCategoryVOS)
                .examQuestions(userExamTaskPaperQuestionVOS)
                .build();
        ExamTaskUserRecordPO examTaskUserRecordPO = ExamTaskUserRecordPO.builder()
                .id(userExamTaskRecord.getId())
                .examTaskId(userExamTaskRecord.getExamTaskId())
                .examPaperId(userExamTaskRecord.getExamPaper().getId())
                .examPaperName(userExamTaskRecord.getExamPaper().getExamPaperInfo().getName())
                .examPaperContent(JsonUtils.serialize(examPaperVO))
                .userId(userExamTaskRecord.getExamTaskUserInfo().getId())
                .userIdentify(userExamTaskRecord.getExamTaskUserInfo().getIdentify())
                .userName(userExamTaskRecord.getExamTaskUserInfo().getName())
                .userPhone(userExamTaskRecord.getExamTaskUserInfo().getPhone())
                .userDepartmentId(userExamTaskRecord.getExamTaskUserInfo().getDepartmentId())
                .userDepartmentName(userExamTaskRecord.getExamTaskUserInfo().getDepartmentName())
                .userPostId(userExamTaskRecord.getExamTaskUserInfo().getPostId())
                .userPostName(userExamTaskRecord.getExamTaskUserInfo().getPostName())
                .submitted(true)
                .passed(userExamTaskRecord.getPassed())
                .score(userExamTaskRecord.getScore())
                .examTime(userExamTaskRecord.getExamTime())
                .examSubmittedTime(userExamTaskRecord.getExamSubmittedTime())
                .examDuration(userExamTaskRecord.getExamDuration())
                .deleted(false)
                .build();
        this.examTaskUserRecordMapper.insert(examTaskUserRecordPO);
        return userExamTaskRecord;
    }
}
