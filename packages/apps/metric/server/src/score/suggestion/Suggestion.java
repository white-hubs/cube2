package com.guhecloud.cube.biz.metric.server.score.suggestion;

import com.guhecloud.cube.biz.metric.server.score.dto.Score;
import com.guhecloud.cube.biz.metric.server.score.dto.TopicCalculateScore;
import com.guhecloud.cube.biz.metric.server.score.suggestion.dto.MetricSuggestionResult;

public interface Suggestion {

    MetricSuggestionResult getMetricSuggestion(TopicCalculateScore topicCalculateScore);

    MetricSuggestionResult getSafetyReportSuggestion(Score score);
}
