package com.guhecloud.cube.biz.metric.server.score.config;

import com.guhecloud.cube.biz.metric.server.score.config.dto.ScoreItems;
import com.guhecloud.cube.biz.metric.server.score.config.dto.ScoreRules;
import com.guhecloud.cube.biz.metric.server.score.param.CalculateParam;

import java.util.Optional;

/**
 * 用来获取主题分数配置
 *
 * @author lanchun
 * @date 2025/6/3 16:28
 */
public interface SafetyReportScoreTopicConfig {

    Optional<ScoreItems> getTopicScoreConfig(CalculateParam param, ScoreRules scoreRules);
}
