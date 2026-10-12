package com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.infrastructure.repository;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.application.ExamPaperQuestionCategoryService;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.application.ExamPaperQuestionService;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.aggregates.ExamPaper;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.entity.ExamPaperInfo;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.entity.ExamPaperQuestionCategoryInfo;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.entity.ExamPaperQuestionInfo;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.enumeration.ExamPaperSourceEnum;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.po.ExamPaperPO;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.po.ExamPaperQuestionCategoryPO;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.po.ExamPaperQuestionPO;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.valueobj.ExamQuestionType;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.valueobj.ThemeInfo;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.infrastructure.repository.mapper.ExamPaperMapper;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.database.audit.EntityAuditor;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ExamPaperRepository {

    private final ExamPaperMapper examPaperMapper;

    private final ExamPaperQuestionCategoryService examPaperQuestionCategoryService;

    private final ExamPaperQuestionService examPaperQuestionService;

    @Transactional(rollbackFor = Exception.class)
    public ExamPaper save(ExamPaper examPaper) {
        boolean isExecuteSave = true;
        // 如果考卷已经存在，那么就更新，否则就执行保存
        ExamPaperPO existExamPaperPO = this.examPaperMapper.selectById(examPaper.getId());
        if (Objects.nonNull(existExamPaperPO)) {
            isExecuteSave = false;
        }
        int examPaperQuestionNumber = examPaper.getExamPaperQuestionCategoryInfos()
                .stream()
                .mapToInt(examPaperQuestionCategoryInfos -> examPaperQuestionCategoryInfos.getExamPaperQuestionInfos().size())
                .sum();
        ExamPaperSourceEnum paperSource = examPaper.getPaperSource() != null ? examPaper.getPaperSource() : ExamPaperSourceEnum.MANUAL;
        ExamPaperPO examPaperPO = ExamPaperPO.builder()
                .id(examPaper.getId())
                .name(examPaper.getExamPaperInfo().getName())
                .score(examPaper.getExamPaperInfo().getScore())
                .passingGradeScore(examPaper.getExamPaperInfo().getPassingGradeScore())
                .remark(examPaper.getExamPaperInfo().getRemark())
                .examPaperQuestionNumber(examPaperQuestionNumber)
                .paperSource(paperSource)
                .deleted(false)
                .build();
        //删除题目大类
        List<ExamPaperQuestionCategoryPO> examPaperQuestionCategoryPOS = this.examPaperQuestionCategoryService.list(Wrappers
                                                                                                                            .<ExamPaperQuestionCategoryPO>lambdaQuery()
                                                                                                                            .eq(ExamPaperQuestionCategoryPO::getExamPaperId, examPaper.getId()));
        if (!CollectionUtils.isEmpty(examPaperQuestionCategoryPOS)) {
            DatabaseContextHolder.softDelete(ExamPaperQuestionCategoryPO.class, examPaperQuestionCategoryPOS);
        }
        //删除题目
        List<ExamPaperQuestionPO> examPaperQuestionPOS = this.examPaperQuestionService.list(Wrappers.<ExamPaperQuestionPO>lambdaQuery()
                                                                                                    .eq(ExamPaperQuestionPO::getExamPaperId, examPaper.getId()));
        if (!CollectionUtils.isEmpty(examPaperQuestionPOS)) {
            DatabaseContextHolder.softDelete(ExamPaperQuestionPO.class, examPaperQuestionPOS);
        }
        List<ExamPaperQuestionCategoryPO> insertExamPaperQuestionCategoryPOS = new ArrayList<>(examPaper.getExamPaperQuestionCategoryInfos()
                                                                                                       .size());
        List<ExamPaperQuestionPO> insertExamPaperQuestionPOS = new ArrayList<>(examPaperQuestionNumber);
        examPaper.getExamPaperQuestionCategoryInfos().forEach(examPaperQuestionCategoryInfo -> {
            insertExamPaperQuestionCategoryPOS.add(ExamPaperQuestionCategoryPO.builder()
                                                           .id(examPaperQuestionCategoryInfo.getId())
                                                           .examPaperId(examPaper.getId())
                                                           .title(examPaperQuestionCategoryInfo.getTitle())
                                                           .sort(examPaperQuestionCategoryInfo.getSort())
                                                           .build());
            examPaperQuestionCategoryInfo.getExamPaperQuestionInfos()
                    .forEach(examPaperQuestionInfo ->
                                     insertExamPaperQuestionPOS.add(ExamPaperQuestionPO
                                                                            .builder()
                                                                            .id(examPaperQuestionInfo.getId())
                                                                            .examPaperId(examPaper.getId())
                                                                            .examPaperQuestionCategoryId(examPaperQuestionCategoryInfo.getId())
                                                                            .name(examPaperQuestionInfo.getName())
                                                                            .themeId(examPaperQuestionInfo
                                                                                             .getThemeInfo()
                                                                                             .getId())
                                                                            .themeName(examPaperQuestionInfo
                                                                                               .getThemeInfo()
                                                                                               .getName())
                                                                            .questionType(examPaperQuestionInfo
                                                                                                  .getQuestionType()
                                                                                                  .getQuestionType())
                                                                            .questionContent(examPaperQuestionInfo.getQuestionContent())
                                                                            .score(examPaperQuestionInfo.getScore())
                                                                            .answerAnalysis(examPaperQuestionInfo.getAnswerAnalysis())
                                                                            .sort(examPaperQuestionInfo.getSort())
                                                                            .build())
                            );
        });
        //新增大题
        if (!CollectionUtils.isEmpty(insertExamPaperQuestionCategoryPOS)) {
            this.examPaperQuestionCategoryService.saveBatch(insertExamPaperQuestionCategoryPOS);
        }
        //新增题目
        if (!CollectionUtils.isEmpty(insertExamPaperQuestionPOS)) {
            this.examPaperQuestionService.saveBatch(insertExamPaperQuestionPOS);
        }
        if (isExecuteSave) {
            this.examPaperMapper.insert(examPaperPO);
        } else {
            EntityAuditor.populateCreateAudit(examPaperPO, existExamPaperPO.getCreateBy(), existExamPaperPO.getCreatorId(),
                                              existExamPaperPO.getCreatorType(), existExamPaperPO.getCreateTime());
            this.examPaperMapper.updateById(examPaperPO);
        }
        return examPaper;
    }

    public Optional<ExamPaper> get(Long id) {
        ExamPaperPO examPaperPO = this.examPaperMapper.selectById(id);
        if (Objects.isNull(examPaperPO)) {
            return Optional.empty();
        }
        //试卷基础信息
        ExamPaperInfo examPaperInfo = new ExamPaperInfo(examPaperPO.getName(), examPaperPO.getScore(), examPaperPO.getPassingGradeScore(), examPaperPO.getRemark());
        //获取试卷大题
        List<ExamPaperQuestionCategoryPO> examPaperQuestionCategoryPOS = this.examPaperQuestionCategoryService.list(Wrappers
                                                                                                                            .<ExamPaperQuestionCategoryPO>lambdaQuery()
                                                                                                                            .eq(ExamPaperQuestionCategoryPO::getExamPaperId, id)
                                                                                                                            .orderByAsc(ExamPaperQuestionCategoryPO::getSort));
        //获取试卷题目(按题目大类分组)
        Map<Long, List<ExamPaperQuestionPO>> examPaperQuestionMaps = this.examPaperQuestionService
                .list(Wrappers.<ExamPaperQuestionPO>lambdaQuery()
                              .eq(ExamPaperQuestionPO::getExamPaperId, id)
                              .orderByAsc(ExamPaperQuestionPO::getSort))
                .stream()
                .collect(Collectors.groupingBy(ExamPaperQuestionPO::getExamPaperQuestionCategoryId));
        //试卷大题试题
        List<ExamPaperQuestionCategoryInfo> examPaperQuestionCategoryInfos = new ArrayList<>(examPaperQuestionCategoryPOS.size());
        examPaperQuestionCategoryPOS.stream().forEach(examPaperQuestionCategoryPO -> {
            //大题题目
            List<ExamPaperQuestionPO> examPaperQuestionPOS = examPaperQuestionMaps.get(examPaperQuestionCategoryPO.getId());
            //大题题目实体
            List<ExamPaperQuestionInfo> examPaperQuestionInfos = new ArrayList<>(examPaperQuestionPOS.size());
            examPaperQuestionPOS.forEach(examPaperQuestionPO ->
                                                 examPaperQuestionInfos.add(new ExamPaperQuestionInfo(
                                                         examPaperQuestionPO.getId(),
                                                         examPaperQuestionPO.getName(),
                                                         new ThemeInfo(examPaperQuestionPO.getThemeId(), null),
                                                         new ExamQuestionType(examPaperQuestionPO.getQuestionType(), examPaperQuestionPO
                                                                 .getQuestionType().getDescription()),
                                                         examPaperQuestionPO.getQuestionContent(),
                                                         examPaperQuestionPO.getScore(),
                                                         examPaperQuestionPO.getAnswerAnalysis(),
                                                         examPaperQuestionPO.getSort()
                                                 ))
                                        );
            examPaperQuestionCategoryInfos.add(new ExamPaperQuestionCategoryInfo(
                    examPaperQuestionCategoryPO.getId(),
                    examPaperQuestionCategoryPO.getTitle(),
                    examPaperQuestionCategoryPO.getSort(),
                    examPaperQuestionInfos
            ));
        });
        return Optional.of(new ExamPaper(examPaperPO.getId(), examPaperInfo, examPaperQuestionCategoryInfos));
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteBatchByIds(Set<Long> examPaperIds) {
        //删除题目大类
        List<ExamPaperQuestionCategoryPO> examPaperQuestionCategoryPOS = this.examPaperQuestionCategoryService.list(Wrappers
                                                                                                                            .<ExamPaperQuestionCategoryPO>lambdaQuery()
                                                                                                                            .in(ExamPaperQuestionCategoryPO::getExamPaperId, examPaperIds));
        if (!CollectionUtils.isEmpty(examPaperQuestionCategoryPOS)) {
            DatabaseContextHolder.softDelete(ExamPaperQuestionCategoryPO.class, examPaperQuestionCategoryPOS);
        }
        //删除题目
        List<ExamPaperQuestionPO> examPaperQuestionPOS = this.examPaperQuestionService.list(Wrappers.<ExamPaperQuestionPO>lambdaQuery()
                                                                                                    .eq(ExamPaperQuestionPO::getExamPaperId, examPaperIds));
        if (!CollectionUtils.isEmpty(examPaperQuestionPOS)) {
            DatabaseContextHolder.softDelete(ExamPaperQuestionPO.class, examPaperQuestionPOS);
        }
        //删除试卷
        DatabaseContextHolder.softDelete(ExamPaperPO.class, examPaperIds);
    }
}
