package com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.application.querygateway;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.srv.gen.safety.education.server.common.application.ThemeService;
import com.guhecloud.cube.srv.gen.safety.education.server.common.domain.po.ThemePO;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.application.querygateway.query.ExamPaperQuery;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.enumeration.ExamPaperSourceEnum;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.po.ExamPaperPO;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.po.ExamPaperQuestionCategoryPO;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.po.ExamPaperQuestionPO;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.infrastructure.repository.mapper.ExamPaperMapper;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.infrastructure.repository.mapper.ExamPaperQuestionCategoryMapper;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.infrastructure.repository.mapper.ExamPaperQuestionMapper;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.interfaces.vo.ExamPaperQuestionCategoryVO;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.interfaces.vo.ExamPaperQuestionVO;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.interfaces.vo.ExamPaperVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import com.guhecloud.platform.base.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.guhecloud.platform.base.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ExamPaperQueryService {

    private final ExamPaperMapper examPaperMapper;

    private final ExamPaperQuestionCategoryMapper examPaperQuestionCategoryMapper;

    private final ThemeService themeService;

    private final ExamPaperQuestionMapper examPaperQuestionMapper;

    /**
     * 试卷详情
     */
    public ExamPaperVO getExamPaper(Long id) {
        ExamPaperPO examPaperPO = this.examPaperMapper.selectById(id);
        Assert.isNotFound(examPaperPO, "试卷不存在");
        return extendExamPaperVO(examPaperPO);
    }

    /**
     * 试卷详情
     *
     * @param id
     *
     * @return
     */
    public ExamPaperVO getExamPaperIfExist(Long id) {
        ExamPaperPO examPaperPO = this.examPaperMapper.selectById(id);
        if (Objects.isNull(examPaperPO)) {
            return null;
        }
        return extendExamPaperVO(examPaperPO);
    }

    /**
     * 试卷列表
     *
     * @param ids
     *
     * @return
     */
    public List<ExamPaperVO> getExamPapersIfExist(Set<Long> ids) {
        List<ExamPaperPO> examPaperPOS = this.examPaperMapper.selectList(Wrappers.<ExamPaperPO>lambdaQuery()
                                                                                 .in(ExamPaperPO::getId, ids));
        if (CollectionUtils.isEmpty(examPaperPOS)) {
            return Collections.emptyList();
        }
        return examPaperPOS.stream().map(this::extendExamPaperVO).collect(Collectors.toList());
    }

    /**
     * 试卷基础信息
     *
     * @param ids
     *
     * @return
     */
    public List<ExamPaperVO> getExamPapersBaseInfoIfExist(Set<Long> ids) {
        List<ExamPaperPO> examPaperPOS = this.examPaperMapper.selectList(Wrappers.<ExamPaperPO>lambdaQuery()
                                                                                 .in(ExamPaperPO::getId, ids));
        if (CollectionUtils.isEmpty(examPaperPOS)) {
            return Collections.emptyList();
        }
        return ObjectUtils.copying(examPaperPOS, ExamPaperVO.class);
    }

    public ExamPaperVO extendExamPaperVO(ExamPaperPO examPaperPO) {
        ExamPaperVO examPaperVO = ObjectUtils.copying(examPaperPO, ExamPaperVO.class);
        //获取试卷大题
        List<ExamPaperQuestionCategoryPO> examPaperQuestionCategoryPOS = this.examPaperQuestionCategoryMapper.selectList(Wrappers
                                                                                                                                 .<ExamPaperQuestionCategoryPO>lambdaQuery()
                                                                                                                                 .eq(ExamPaperQuestionCategoryPO::getExamPaperId, examPaperPO.getId())
                                                                                                                                 .orderByAsc(ExamPaperQuestionCategoryPO::getSort));
        //获取试卷题目(按题目大类分组)
        List<ExamPaperQuestionPO> examPaperQuestionPOS = this.examPaperQuestionMapper.selectList(Wrappers.<ExamPaperQuestionPO>lambdaQuery()
                                                                                                         .eq(ExamPaperQuestionPO::getExamPaperId, examPaperPO.getId())
                                                                                                         .orderByAsc(ExamPaperQuestionPO::getSort));
        Map<Long, List<ExamPaperQuestionPO>> examPaperQuestionMaps = examPaperQuestionPOS.stream()
                .collect(Collectors.groupingBy(ExamPaperQuestionPO::getExamPaperQuestionCategoryId));
        //获取题目主题
        Set<Long> questionThemeIds = examPaperQuestionPOS.stream().map(ExamPaperQuestionPO::getThemeId).collect(Collectors.toSet());
        List<ThemePO> themePOS = this.themeService.list(Wrappers.<ThemePO>lambdaQuery().in(ThemePO::getId, questionThemeIds));
        List<ExamPaperQuestionCategoryVO> examPaperQuestionCategoryVOS = new ArrayList<>(examPaperQuestionCategoryPOS.size());
        examPaperQuestionCategoryPOS.stream().forEach(examPaperQuestionCategoryPO -> {
            List<ExamPaperQuestionPO> categoryExamPaperQuestionPOS = examPaperQuestionMaps.get(examPaperQuestionCategoryPO.getId());
            ExamPaperQuestionCategoryVO examPaperQuestionCategoryVO = ObjectUtils.copying(examPaperQuestionCategoryPO, ExamPaperQuestionCategoryVO.class);
            List<ExamPaperQuestionVO> examPaperQuestionVOS = new ArrayList<>(examPaperQuestionPOS.size());
            categoryExamPaperQuestionPOS.stream().forEach(examPaperQuestionPO -> {
                ExamPaperQuestionVO examPaperQuestionVO = ObjectUtils.copying(examPaperQuestionPO, ExamPaperQuestionVO.class);
                examPaperQuestionVO.setExamPaperQuestionCategoryTitle(examPaperQuestionCategoryPO.getTitle());
                examPaperQuestionVO.setQuestionTypeName(examPaperQuestionPO.getQuestionType().getDescription());
                Optional<ThemePO> questionThemePO = themePOS.stream()
                        .filter(themePO -> examPaperQuestionPO.getThemeId().equals(themePO.getId())).findFirst();
                questionThemePO.ifPresent(themePO -> examPaperQuestionVO.setThemeName(themePO.getName()));
                examPaperQuestionVOS.add(examPaperQuestionVO);
            });
            //大题下的题目
            examPaperQuestionCategoryVO.setExamQuestions(examPaperQuestionVOS);
            examPaperQuestionCategoryVOS.add(examPaperQuestionCategoryVO);
        });
        examPaperVO.setQuestionCategories(examPaperQuestionCategoryVOS);
        return examPaperVO;
    }

    /**
     * 试卷大题
     *
     * @param examPaperQuery
     *
     * @return
     */
    public Pagination<ExamPaperVO> getExamPaperPage(ExamPaperQuery examPaperQuery) {
        Page<ExamPaperPO> page = this.examPaperMapper.selectPage(PagingUtils.convert(examPaperQuery), Wrappers.<ExamPaperPO>lambdaQuery()
                .and(w -> w.eq(ExamPaperPO::getPaperSource, ExamPaperSourceEnum.MANUAL).or().isNull(ExamPaperPO::getPaperSource))
                .like(StringUtils.isNotEmpty(examPaperQuery.getKeyword()), ExamPaperPO::getName, examPaperQuery.getKeyword())
                .orderByDesc(ExamPaperPO::getCreateTime));
        return PagingUtils.convert(page, ExamPaperVO.class);
    }
}
