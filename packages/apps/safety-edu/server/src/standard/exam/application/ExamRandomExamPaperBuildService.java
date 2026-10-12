package com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.application;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.srv.gen.safety.education.server.common.application.ThemeService;
import com.guhecloud.cube.srv.gen.safety.education.server.common.application.querygateway.ExamQuestionRuleQueryService;
import com.guhecloud.cube.srv.gen.safety.education.server.common.domain.po.ThemePO;
import com.guhecloud.cube.srv.gen.safety.education.server.common.interfaces.vo.ExamQuestionRuleItemVO;
import com.guhecloud.cube.srv.gen.safety.education.server.common.interfaces.vo.ExamQuestionRuleVO;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.aggregates.ExamPaper;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.entity.ExamPaperInfo;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.entity.ExamPaperQuestionCategoryInfo;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.entity.ExamPaperQuestionInfo;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.po.ExamQuestionBankPO;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.po.ExamTaskPaperPO;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.valueobj.ExamQuestionType;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.valueobj.ThemeInfo;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.interfaces.vo.ExamPaperQuestionCategoryVO;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.interfaces.vo.ExamPaperQuestionVO;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.interfaces.vo.ExamPaperVO;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 按考题规则随机组卷并落库，供考试任务使用。
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ExamRandomExamPaperBuildService {

    private final ExamQuestionRuleQueryService examQuestionRuleQueryService;

    private final ThemeService themeService;

    private final ExamQuestionBankService examQuestionBankService;

    private final ExamTaskPaperService examTaskPaperService;

    /**
     * 删除某任务此前随机生成的试卷（软删）
     */
    public void removeRandomPapersByExamTaskId(Long examTaskId) {
        this.removeRandomPapersByExamTaskIdAndUserIdentifies(examTaskId, null);
    }

    public void removeRandomPapersByExamTaskIdAndUserIdentify(Long examTaskId, String userIdentify) {
        Set<String> userIdentifies = StringUtils.isBlank(userIdentify) ? Collections.emptySet() : Collections.singleton(userIdentify);
        this.removeRandomPapersByExamTaskIdAndUserIdentifies(examTaskId, userIdentifies);
    }

    public void removeRandomPapersByExamTaskIdAndUserIdentifies(Long examTaskId, Set<String> userIdentifies) {
        if (examTaskId == null) {
            return;
        }
        var query = Wrappers.<ExamTaskPaperPO>lambdaQuery().eq(ExamTaskPaperPO::getExamTaskId, examTaskId);
        if (CollectionUtils.isNotEmpty(userIdentifies)) {
            query.in(ExamTaskPaperPO::getUserIdentify, userIdentifies);
        }
        List<ExamTaskPaperPO> links = this.examTaskPaperService.list(query);
        if (CollectionUtils.isEmpty(links)) {
            return;
        }
        DatabaseContextHolder.softDelete(ExamTaskPaperPO.class, links);
    }

    /**
     * 按考题规则主键集合生成多张试卷（每张规则一张），并写入试卷管理。
     */
    public List<ExamPaperVO> generateAndPersistPapers(Long examTaskId, Set<Long> ruleIds) {
        Assert.isNotAcceptable(CollectionUtils.isEmpty(ruleIds), "考题规则不能为空");
        List<ExamQuestionRuleVO> rules = this.examQuestionRuleQueryService.listExamQuestionRulesByIds(ruleIds);
        Assert.isNotAcceptable(rules.size() != ruleIds.size(), "存在无效的考题规则");
        List<ExamPaperVO> result = new ArrayList<>(rules.size());
        List<Long> orderedRuleIds = ruleIds.stream().sorted(Comparator.naturalOrder()).collect(Collectors.toList());
        Map<Long, ExamQuestionRuleVO> ruleMap = rules.stream().collect(Collectors.toMap(ExamQuestionRuleVO::getId, r -> r));
        for (Long ruleId : orderedRuleIds) {
            ExamQuestionRuleVO rule = ruleMap.get(ruleId);
            ExamPaper examPaper = buildExamPaperFromRule(rule);
            ExamPaperVO vo = convertToExamPaperVO(examPaper);
            vo.setRuleId(ruleId);
            result.add(vo);
        }
        return result;
    }

    /**
     * 为用户按规则集合生成试卷（不写任务关联关系，由调用方绑定用户）。
     */
    public List<ExamPaperVO> generateAndPersistPapersForUser(Long examTaskId, Set<Long> ruleIds) {
        return this.generateAndPersistPapers(examTaskId, ruleIds);
    }

    private ExamPaperVO convertToExamPaperVO(ExamPaper examPaper) {
        List<ExamPaperQuestionCategoryVO> categories = examPaper.getExamPaperQuestionCategoryInfos().stream()
                .map(this::convertCategoryVO)
                .collect(Collectors.toList());
        int questionNumber = categories.stream()
                .map(ExamPaperQuestionCategoryVO::getExamQuestions)
                .filter(Objects::nonNull)
                .mapToInt(List::size)
                .sum();
        ExamPaperInfo info = examPaper.getExamPaperInfo();
        List<ExamPaperQuestionVO> examPaperQuestionVOS = new ArrayList<>();
        categories.forEach(examPaperQuestionCategoryVO ->
                                   examPaperQuestionVOS.addAll(examPaperQuestionCategoryVO.getExamQuestions())
                          );
        return ExamPaperVO.builder()
                .id(examPaper.getId())
                .name(info.getName())
                .score(info.getScore())
                .passingGradeScore(info.getPassingGradeScore())
                .remark(info.getRemark())
                .examPaperQuestionNumber(questionNumber)
                .questionCategories(categories)
                .examQuestions(examPaperQuestionVOS)
                .build();
    }

    private ExamPaperQuestionCategoryVO convertCategoryVO(ExamPaperQuestionCategoryInfo categoryInfo) {
        List<ExamPaperQuestionVO> questionVOS = categoryInfo.getExamPaperQuestionInfos().stream()
                .map(questionInfo -> convertQuestionVO(categoryInfo, questionInfo))
                .collect(Collectors.toList());
        return ExamPaperQuestionCategoryVO.builder()
                .id(categoryInfo.getId())
                .title(categoryInfo.getTitle())
                .sort(categoryInfo.getSort())
                .examQuestions(questionVOS)
                .build();
    }

    private ExamPaperQuestionVO convertQuestionVO(ExamPaperQuestionCategoryInfo categoryInfo, ExamPaperQuestionInfo questionInfo) {
        return ExamPaperQuestionVO.builder()
                .id(questionInfo.getId())
                .name(questionInfo.getName())
                .examPaperQuestionCategoryId(categoryInfo.getId())
                .examPaperQuestionCategoryTitle(categoryInfo.getTitle())
                .themeId(questionInfo.getThemeInfo() != null ? questionInfo.getThemeInfo().getId() : null)
                .themeName(questionInfo.getThemeInfo() != null ? questionInfo.getThemeInfo().getName() : null)
                .questionType(questionInfo.getQuestionType() != null ? questionInfo.getQuestionType().getQuestionType() : null)
                .questionTypeName(questionInfo.getQuestionType() != null ? questionInfo.getQuestionType().getQuestionTypeName() : null)
                .questionContent(questionInfo.getQuestionContent())
                .score(questionInfo.getScore())
                .answerAnalysis(questionInfo.getAnswerAnalysis())
                .sort(questionInfo.getSort())
                .build();
    }

    private ExamPaper buildExamPaperFromRule(ExamQuestionRuleVO rule) {
        Assert.isNotAcceptable(CollectionUtils.isEmpty(rule.getItems()), "考题规则明细不能为空");
        List<List<ExamQuestionBankPO>> banksPerItem = new ArrayList<>(rule.getItems().size());
        for (ExamQuestionRuleItemVO item : rule.getItems()) {
            banksPerItem.add(pickQuestionsForItem(item));
        }
        Set<Long> themeIdsToLoad = banksPerItem.stream()
                .flatMap(List::stream)
                .map(ExamQuestionBankPO::getThemeId)
                .filter(Objects::nonNull)
                .collect(Collectors.toCollection(LinkedHashSet::new));
        Map<Long, String> themeIdToName = Collections.emptyMap();
        if (CollectionUtils.isNotEmpty(themeIdsToLoad)) {
            List<ThemePO> themePOS = this.themeService.listByIds(themeIdsToLoad);
            themeIdToName = themePOS.stream()
                    .collect(Collectors.toMap(ThemePO::getId, t -> t.getName() == null ? "" : t.getName(), (a, b) -> a));
        }
        List<ExamPaperQuestionCategoryInfo> categories = new ArrayList<>(rule.getItems().size());
        for (int i = 0; i < rule.getItems().size(); i++) {
            ExamQuestionRuleItemVO item = rule.getItems().get(i);
            List<ExamQuestionBankPO> banks = banksPerItem.get(i);
            String categoryTitle = item.getExamQuestionType().getDescription();
            float per = item.getPerScore().floatValue();
            List<ExamPaperQuestionInfo> sectionQuestions = new ArrayList<>(banks.size());
            int qSort = 1;
            for (ExamQuestionBankPO bank : banks) {
                Long themeId = bank.getThemeId();
                String themeName = themeId != null ? themeIdToName.getOrDefault(themeId, "") : "";
                sectionQuestions.add(new ExamPaperQuestionInfo(
                        IdWorker.getId(),
                        bank.getName(),
                        new ThemeInfo(themeId, themeName),
                        new ExamQuestionType(bank.getQuestionType(), bank.getQuestionType().getDescription()),
                        bank.getQuestionContent(),
                        per,
                        bank.getAnswerAnalysis(),
                        qSort++
                ));
            }
            categories.add(new ExamPaperQuestionCategoryInfo(
                    IdWorker.getId(),
                    categoryTitle,
                    i + 1,
                    sectionQuestions));
        }
        ExamPaperInfo info = new ExamPaperInfo(
                "随机考题试卷",
                rule.getTotalScore().floatValue(),
                rule.getPassScore().floatValue(),
                rule.getDescription());
        return new ExamPaper(IdWorker.getId(), info, categories);
    }

    private List<ExamQuestionBankPO> pickQuestionsForItem(ExamQuestionRuleItemVO item) {
        Assert.isNotAcceptable(item.getExamQuestionType() == null, "规则项题目类型不能为空");
        List<Long> themeIds = parseCommaSeparatedThemeIds(item.getThemeKeywords());
        Assert.isNotAcceptable(themeIds.isEmpty(), "规则项题目主题不能为空");
        List<ExamQuestionBankPO> candidates = this.examQuestionBankService.list(Wrappers.<ExamQuestionBankPO>lambdaQuery()
                                                                                        .select(ExamQuestionBankPO::getId)
                                                                                        .in(ExamQuestionBankPO::getThemeId, themeIds)
                                                                                        .eq(ExamQuestionBankPO::getQuestionType, item.getExamQuestionType()));
        Assert.isNotAcceptable(CollectionUtils.isEmpty(candidates), "题库中无满足主题与题目类型的题目");
        List<Long> questionIds = candidates.stream().map(ExamQuestionBankPO::getId).collect(Collectors.toList());
        Assert.isNotAcceptable(questionIds.size() < item.getExtractCount(), "题库可用题目数量不足，无法抽取指定数量");
        List<Long> shuffled = new ArrayList<>(questionIds);
        Collections.shuffle(shuffled, ThreadLocalRandom.current());
        List<Long> randomQuestionIds = shuffled.subList(0, item.getExtractCount());
        List<ExamQuestionBankPO> loaded = this.examQuestionBankService.lambdaQuery()
                .in(ExamQuestionBankPO::getId, randomQuestionIds)
                .list();
        Map<Long, ExamQuestionBankPO> byId = loaded.stream()
                .collect(Collectors.toMap(ExamQuestionBankPO::getId, Function.identity(), (a, b) -> a));
        List<ExamQuestionBankPO> ordered = new ArrayList<>(randomQuestionIds.size());
        for (Long qid : randomQuestionIds) {
            ExamQuestionBankPO bank = byId.get(qid);
            if (bank != null) {
                ordered.add(bank);
            }
        }
        return ordered;
    }

    /**
     * 题目主题字段存的是主题主键 id，多个 id 逗号分隔。
     */
    private static List<Long> parseCommaSeparatedThemeIds(String raw) {
        if (StringUtils.isBlank(raw)) {
            return Collections.emptyList();
        }
        LinkedHashSet<Long> ids = new LinkedHashSet<>();
        for (String p : raw.split(",")) {
            String t = p.trim();
            if (StringUtils.isEmpty(t)) {
                continue;
            }
            try {
                ids.add(Long.parseLong(t));
            } catch (NumberFormatException e) {
                Assert.isNotAcceptable(true, "规则项题目主题包含非法编号: " + t);
            }
        }
        return new ArrayList<>(ids);
    }
}
