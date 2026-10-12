package com.guhecloud.cube.biz.metric.server.score.config;

import com.guhecloud.cube.biz.metric.server.score.config.dto.ScoreRules;

import java.util.Optional;

public interface SafetyReportConfig {

    Optional<ScoreRules> getScoreRules();
}
