package com.guhecloud.cube.biz.metric.server.score.safetyreport.repository;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.biz.metric.server.score.dto.TopicCalculateScore;
import com.guhecloud.cube.biz.metric.server.score.safetyreport.repository.dao.MetricScoreMapper;
import com.guhecloud.cube.biz.metric.server.score.safetyreport.repository.po.RuleScorePO;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.CompressUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Repository
public class RuleScoreRepositoryImpl implements RuleScoreRepository {

    private final MetricScoreMapper metricScoreMapper;

    public RuleScoreRepositoryImpl(MetricScoreMapper metricScoreMapper) {
        this.metricScoreMapper = metricScoreMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveRuleScore(TopicCalculateScore topicCalculateScore) {
        TopicCalculateScore score = getRuleScore(topicCalculateScore.getRequestId(), topicCalculateScore.getTopicConcat());
        if (Objects.nonNull(score)) {
            delRuleScore(topicCalculateScore.getRequestId(), topicCalculateScore.getTopicConcat());
        }
        RuleScorePO ruleScorePO = new RuleScorePO();
        ruleScorePO.setTopic(topicCalculateScore.getTopic());
        ruleScorePO.setTopicConcat(topicCalculateScore.getTopicConcat());
        ruleScorePO.setScore(topicCalculateScore.getScore());
        ruleScorePO.setWeightScore(topicCalculateScore.getWeightScore());
        ruleScorePO.setRequestId(topicCalculateScore.getRequestId());
        ruleScorePO.setBeginTime(topicCalculateScore.getBeginTime());
        ruleScorePO.setEndTime(topicCalculateScore.getEndTime());
        ruleScorePO.setSuggestion(topicCalculateScore.getSuggestion());
        ruleScorePO.setCreateTime(topicCalculateScore.getCreateTime());
        if (Objects.nonNull(topicCalculateScore.getMetricResult())) {
            try {
                ruleScorePO.setData(CompressUtils.gzip(Objects.requireNonNull(JsonUtils.serialize(topicCalculateScore))));
            } catch (IOException e) {
                log.error("规则指标原数据压缩失败:", e);
            }
        }
        return metricScoreMapper.insert(ruleScorePO) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delRuleScore(String requestId, String ruleCodeConcat) {
        List<RuleScorePO> ruleScorePOS = this.metricScoreMapper.selectList(Wrappers.<RuleScorePO>lambdaQuery()
                                                                                   .eq(RuleScorePO::getTopicConcat, ruleCodeConcat)
                                                                                   .eq(RuleScorePO::getRequestId, requestId)
                                                                          );
        if (!CollectionUtils.isEmpty(ruleScorePOS)) {
            Set<Long> ids = ruleScorePOS.stream().map(RuleScorePO::getId).collect(Collectors.toSet());
            DatabaseContextHolder.softDelete(RuleScorePO.class, ids);
        }
    }

    @Override
    public TopicCalculateScore getRuleScore(String requestId, String ruleCodeConcat) {
        List<RuleScorePO> ruleScorePOS = this.metricScoreMapper.selectList(Wrappers.<RuleScorePO>lambdaQuery()
                                                                                   .eq(RuleScorePO::getTopicConcat, ruleCodeConcat)
                                                                                   .eq(RuleScorePO::getRequestId, requestId)
                                                                          );
        if (CollectionUtils.isEmpty(ruleScorePOS)) {
            return null;
        }
        return convert(ruleScorePOS.get(0));
    }

    @Override
    public List<TopicCalculateScore> getRuleScores(String requestId) {
        List<RuleScorePO> ruleScorePOS = this.metricScoreMapper.selectList(Wrappers.<RuleScorePO>lambdaQuery()
                                                                                   .eq(RuleScorePO::getRequestId, requestId));
        return ruleScorePOS.stream().map(this::convert).collect(Collectors.toList());
    }

    @Override
    public List<TopicCalculateScore> getRuleScores(String requestId, String leftRuleCodeConcat) {
        List<RuleScorePO> ruleScorePOS = this.metricScoreMapper.selectList(Wrappers.<RuleScorePO>lambdaQuery()
                                                                                   .eq(RuleScorePO::getRequestId, requestId)
                                                                                   .likeLeft(RuleScorePO::getTopicConcat, leftRuleCodeConcat));
        return ruleScorePOS.stream().map(this::convert).filter(Objects::nonNull).collect(Collectors.toList());
    }

    public TopicCalculateScore convert(RuleScorePO ruleScorePO) {
        try {
            return JsonUtils.deserialize(CompressUtils.ungzip(ruleScorePO.getData()), TopicCalculateScore.class);
        } catch (IOException e) {
            log.error("解压数据失败:", e);
        }
        return null;
    }
}
