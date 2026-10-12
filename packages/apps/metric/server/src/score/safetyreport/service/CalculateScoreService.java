package com.guhecloud.cube.biz.metric.server.score.safetyreport.service;

import com.guhecloud.cube.biz.metric.server.metric.calculate.MetricCalculate;
import com.guhecloud.cube.biz.metric.server.metric.calculate.dto.CalculateResult;
import com.guhecloud.cube.biz.metric.server.metric.result.param.business.MetricParam;
import com.guhecloud.cube.biz.metric.server.score.config.SafetyReportConfigService;
import com.guhecloud.cube.biz.metric.server.score.config.dto.ScoreItems;
import com.guhecloud.cube.biz.metric.server.score.dto.Score;
import com.guhecloud.cube.biz.metric.server.score.dto.TopicCalculateScore;
import com.guhecloud.cube.biz.metric.server.score.param.CalculateParam;
import com.guhecloud.cube.biz.metric.server.score.provider.BaseCalculateScoreProvider;
import com.guhecloud.cube.biz.metric.server.score.safetyreport.repository.RuleScoreRepository;
import com.guhecloud.cube.biz.metric.server.score.safetyreport.repository.SafetyReportScoreRepository;
import com.guhecloud.cube.biz.metric.server.score.suggestion.Suggestion;
import com.guhecloud.cube.biz.metric.server.score.suggestion.dto.MetricSuggestionResult;
import com.guhecloud.cube.biz.ouaa.client.TenantClient;
import com.guhecloud.cube.biz.ouaa.client.dto.TenantDTO;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CalculateScoreService extends BaseCalculateScoreProvider {

    private final MetricCalculate metricCalculate;

    private final RuleScoreRepository ruleScoreRepository;

    private final SafetyReportScoreRepository safetyReportScoreRepository;

    private final TenantClient tenantClient;

    private final Suggestion suggestion;

    public CalculateScoreService(MetricCalculate metricCalculate,
                                 SafetyReportConfigService safetyReportConfigService,
                                 RuleScoreRepository ruleScoreRepository,
                                 Suggestion suggestion,
                                 SafetyReportScoreRepository safetyReportScoreRepository,
                                 TenantClient tenantClient
                                ) {
        super(safetyReportConfigService);
        this.metricCalculate = metricCalculate;
        this.ruleScoreRepository = ruleScoreRepository;
        this.suggestion = suggestion;
        this.safetyReportScoreRepository = safetyReportScoreRepository;
        this.tenantClient = tenantClient;
    }

    /**
     * 获取规则结果建议
     *
     * @return 指标结果
     */
    public TopicCalculateScore calculateRuleScore(CalculateParam param) {
        //存储指标得分
        TopicCalculateScore score = this.calculateScore(param);
        if (Objects.nonNull(score)) {
            ruleScoreRepository.saveRuleScore(score);
        }
        return score;
    }

    /**
     * 获取规则结果建议
     *
     * @param param
     *
     * @return
     */
    public TopicCalculateScore getRuleResultSuggestion(CalculateParam param) {
        TopicCalculateScore metricScore = ruleScoreRepository.getRuleScore(param.getRequestId(), param.getItemCodeConcat());
        if (Objects.isNull(metricScore)) {
            return null;
        }
        //获取建议
        MetricSuggestionResult metricSuggestion = suggestion.getMetricSuggestion(metricScore);
        if (Objects.nonNull(metricSuggestion)) {
            metricScore.setSuggestion(metricSuggestion.getSuggestion());
        }
        ruleScoreRepository.saveRuleScore(metricScore);
        return metricScore;
    }

    /**
     * 获取规则内容
     *
     * @param param
     *
     * @return
     */
    public TopicCalculateScore getRuleContent(CalculateParam param) {
        Optional<ScoreItems> topicScoreConfig = getTopicScoreConfig(param);
        TopicCalculateScore metricScore = ruleScoreRepository.getRuleScore(param.getRequestId(), param.getItemCodeConcat());
        if (Objects.isNull(metricScore) || !topicScoreConfig.isPresent()) {
            return null;
        }
        //获取内容变量替换
        ScoreItems scoreItem = topicScoreConfig.get();
        metricScore.setContentTemplate(scoreItem.getRuleContentTemplate());
        if (StringUtils.isEmpty(scoreItem.getTemplateMetricVariables())) {
            ruleScoreRepository.saveRuleScore(metricScore);
            return metricScore;
        }
        Map<String, Object> contentVariables = new HashMap<>();
        Set<String> metricIds = Arrays.stream(scoreItem.getTemplateMetricVariables().split(",")).collect(Collectors.toSet());
        for (String metricId : metricIds) {
            param.setComputeMetric(Long.valueOf(metricId));
            Object evaluate = metricCalculate.getEvaluate(param);
            contentVariables.put(metricId, evaluate);
        }
        metricScore.setContentVariables(contentVariables);
        ruleScoreRepository.saveRuleScore(metricScore);
        return metricScore;
    }

    /**
     * 获取父级项分数
     *
     * @param param
     *
     * @return
     */
    public TopicCalculateScore calculateParentItemScore(CalculateParam param) {
        Optional<ScoreItems> appScoreConfigOptional = getTopicScoreConfig(param);
        if (!appScoreConfigOptional.isPresent()) {
            return null;
        }
        ScoreItems appScoreConfig = appScoreConfigOptional.get();
        List<TopicCalculateScore> metricScores = ruleScoreRepository.getRuleScores(param.getRequestId(), param.getItemCodeConcat());
        // 构建树形结构
        TopicCalculateScore rootScore = buildTree(param, appScoreConfig, metricScores);
        // 计算根节点的权重分和得分
        double totalWeightScore = rootScore.getSubTopicCalculateScores().stream()
                .filter(topicScore -> Objects.nonNull(topicScore.getWeightScore()))
                .mapToDouble(TopicCalculateScore::getWeightScore)
                .sum();
        double score = 0.0;
        for (TopicCalculateScore metricScore : rootScore.getSubTopicCalculateScores()) {
            if (Objects.isNull(metricScore.getWeightScore())) {
                continue;
            }
            score += BigDecimal.valueOf(metricScore.getScore())
                    .multiply(BigDecimal.valueOf(metricScore.getWeightAllot()))
                    .multiply(BigDecimal.valueOf(0.01))
                    .setScale(2, RoundingMode.HALF_UP)
                    .doubleValue();
        }
        rootScore.setTopicName(appScoreConfig.getItemName());
        rootScore.setWeightScore(totalWeightScore);
        rootScore.setScore(score);
        return rootScore;
    }

    private TopicCalculateScore buildTree(CalculateParam param, ScoreItems scoreItem, List<TopicCalculateScore> metricScores) {
        // 过滤出相关的 metricScores
        Optional<TopicCalculateScore> topicCalculateScoreOpt = metricScores.stream()
                .filter(score -> score.getTopicConcat().equals(scoreItem.getItemCodeConcat()))
                .findFirst();
        if (topicCalculateScoreOpt.isPresent()) {
            return topicCalculateScoreOpt.get();
        }
        TopicCalculateScore topicCalculateScore = new TopicCalculateScore();
        topicCalculateScore.setRequestId(param.getRequestId());
        topicCalculateScore.setTopic(scoreItem.getItemCode());
        topicCalculateScore.setTopicName(scoreItem.getItemName());
        topicCalculateScore.setTopicConcat(scoreItem.getItemCodeConcat());
        topicCalculateScore.setWeightAllot(scoreItem.getWeightAllot());
        topicCalculateScore.setScore(null);
        topicCalculateScore.setWeightScore(null);
        if (CollectionUtils.isEmpty(scoreItem.getChildren())) {
            return topicCalculateScore;
        }
        // 递归构建子树
        List<TopicCalculateScore> subTopicScores = scoreItem.getChildren().stream()
                .map(child -> buildTree(param, child, metricScores))
                .collect(Collectors.toList());
        // 计算根节点的权重分和得分
        double totalWeightScore = subTopicScores.stream()
                .filter(topicScore -> Objects.nonNull(topicScore.getWeightScore()))
                .mapToDouble(TopicCalculateScore::getWeightScore)
                .sum();
        double score = 0.0;
        for (TopicCalculateScore metricScore : subTopicScores) {
            if (Objects.isNull(metricScore.getWeightScore())) {
                continue;
            }
            score += BigDecimal.valueOf(metricScore.getScore())
                    .multiply(BigDecimal.valueOf(metricScore.getWeightAllot()))
                    .multiply(BigDecimal.valueOf(0.01))
                    .setScale(2, RoundingMode.HALF_UP)
                    .doubleValue();
        }
        var createTime = LocalDateTime.now();
        if (subTopicScores.stream().findFirst().isPresent()) {
            createTime = subTopicScores.stream().findFirst().get().getCreateTime();
        }
        topicCalculateScore.setCreateTime(createTime);
        topicCalculateScore.setWeightScore(totalWeightScore);
        topicCalculateScore.setScore(score);
        // 设置子节点
        topicCalculateScore.setSubTopicCalculateScores(subTopicScores);
        return topicCalculateScore;
    }

    /**
     * 计算总分
     *
     * @param param
     *
     * @return
     */
    public Score calculateTotalScore(CalculateParam param) {
        List<TopicCalculateScore> metricScores = ruleScoreRepository.getRuleScores(param.getRequestId());
        Map<String, List<TopicCalculateScore>> oneLevelData = metricScores.stream()
                .collect(Collectors.groupingBy(x -> x.getTopicConcat().split("::")[0]));
        List<TopicCalculateScore> apps = oneLevelData.keySet().stream()
                .map(topicCalculateScores -> {
                    param.setItemCodeConcat(topicCalculateScores);
                    return calculateParentItemScore(param);
                })
                .collect(Collectors.toList());
        double total = metricScores.stream().filter(topicCalculateScore -> Objects.nonNull(topicCalculateScore.getWeightScore()))
                .mapToDouble(TopicCalculateScore::getWeightScore).sum();
        TopicCalculateScore topicScore = metricScores.getFirst();
        TenantDTO tenant = tenantClient.getByCode(SessionContextHolder.getRequester().getTenant());
        String reportName = tenant.getName() + DateUtils.format(topicScore.getBeginTime(), DateUtils.PURE_DATE_FORMAT) + "-"
                            + DateUtils.format(topicScore.getEndTime(), DateUtils.PURE_DATE_FORMAT) + "安全生产情况报告";
        Score score = new Score();
        score.setRequestId(param.getRequestId());
        score.setName(reportName);
        score.setBeginTime(topicScore.getBeginTime());
        score.setEndTime(topicScore.getEndTime());
        score.setCreateTime(metricScores.stream().sorted(Comparator.comparing(TopicCalculateScore::getCreateTime))
                                    .toList().getFirst().getCreateTime());
        score.setScore(total);
        score.setItems(apps.stream().sorted(Comparator.comparing(TopicCalculateScore::getCreateTime)).collect(Collectors.toList()));
        this.safetyReportScoreRepository.saveScore(score);
        return score;
    }

    /**
     * 获取总报告建议
     *
     * @param param
     *
     * @return
     */
    public Score getSafetyResultSuggestion(CalculateParam param) {
        Score score = this.safetyReportScoreRepository.getScoreByRequestId(param.getRequestId());
        if (Objects.isNull(score)) {
            return null;
        }
        MetricSuggestionResult metricSuggestion = suggestion.getSafetyReportSuggestion(score);
        if (Objects.nonNull(metricSuggestion)) {
            score.setSuggestion(metricSuggestion.getSuggestion());
        }
        safetyReportScoreRepository.saveScore(score);
        return score;
    }

    @Override
    protected TopicCalculateScore calculateResult(CalculateParam param) {
        MetricParam metricParam = new MetricParam();
        metricParam.setRequestId(param.getRequestId());
        metricParam.setPeriod(param.getPeriod());
        //获取指标结算结果
        CalculateResult calculateResult = metricCalculate.calculate(param);
        return TopicCalculateScore.builder()
                .requestId(param.getRequestId())
                .beginTime(param.getPeriod().getBegin())
                .endTime(param.getPeriod().getEnd())
                .topicConcat(param.getItemCodeConcat())
                .result(calculateResult.getResult())
                .metricResult(calculateResult)
                .createTime(LocalDateTime.now())
                .build();
    }
}
