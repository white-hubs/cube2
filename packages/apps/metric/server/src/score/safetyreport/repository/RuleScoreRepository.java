package com.guhecloud.cube.biz.metric.server.score.safetyreport.repository;

import com.guhecloud.cube.biz.metric.server.score.dto.TopicCalculateScore;

import java.util.List;

public interface RuleScoreRepository {

    Boolean saveRuleScore(TopicCalculateScore topicCalculateScore);

    TopicCalculateScore getRuleScore(String requestId, String ruleCodeConcat);

    void delRuleScore(String requestId, String ruleCodeConcat);

    List<TopicCalculateScore> getRuleScores(String requestId);

    List<TopicCalculateScore> getRuleScores(String requestId, String leftRuleCodeConcat);
}
