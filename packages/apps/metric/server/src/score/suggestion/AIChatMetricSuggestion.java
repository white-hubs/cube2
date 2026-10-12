package com.guhecloud.cube.biz.metric.server.score.suggestion;

import com.guhecloud.cube.biz.metric.server.score.dto.Score;
import com.guhecloud.cube.biz.metric.server.score.dto.TopicCalculateScore;
import com.guhecloud.cube.biz.metric.server.score.suggestion.ai.service.ChatQueryService;
import com.guhecloud.cube.biz.metric.server.score.suggestion.ai.service.dto.ChatQueryResult;
import com.guhecloud.cube.biz.metric.server.score.suggestion.dto.MetricSuggestionQuery;
import com.guhecloud.cube.biz.metric.server.score.suggestion.dto.MetricSuggestionResult;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Component
public class AIChatMetricSuggestion implements Suggestion {

    private final ChatQueryService chatQueryService;

    public AIChatMetricSuggestion(ChatQueryService chatQueryService) {
        this.chatQueryService = chatQueryService;
    }

    @Override
    public MetricSuggestionResult getMetricSuggestion(TopicCalculateScore topicCalculateScore) {
        MetricSuggestionQuery.MetricDate currentData = MetricSuggestionQuery.MetricDate.builder()
                .beginTime(topicCalculateScore.getBeginTime())
                .endTime(topicCalculateScore.getEndTime())
                .data(topicCalculateScore.getMetricResult())
                .build();
        List<MetricSuggestionQuery.MetricDate> dates = new ArrayList<>();
        dates.add(currentData);
        MetricSuggestionQuery suggestionQuery = new MetricSuggestionQuery();
        suggestionQuery.setMetric(topicCalculateScore.getTopic());
        suggestionQuery.setDates(dates);
        try {
            ChatQueryResult chatQueryResult = chatQueryService.textQuery(JsonUtils.serialize(suggestionQuery));
            return new MetricSuggestionResult(topicCalculateScore.getTopic(), chatQueryResult.getAnswer());
        } catch (Exception e) {
            log.error("指标ai分析失败:", e);
        }
        return new MetricSuggestionResult(topicCalculateScore.getTopic(), StringUtils.EMPTY_STRING);
    }

    @Override
    public MetricSuggestionResult getSafetyReportSuggestion(Score score) {
        MetricSuggestionQuery.MetricDate metricDate = new MetricSuggestionQuery.MetricDate();
        metricDate.setBeginTime(score.getBeginTime());
        metricDate.setEndTime(score.getEndTime());
        metricDate.setData(score.getItems());
        MetricSuggestionQuery suggestionQuery = new MetricSuggestionQuery();
        suggestionQuery.setMetric(score.getName());
        suggestionQuery.setDates(Collections.singletonList(metricDate));
        try {
            ChatQueryResult chatQueryResult = chatQueryService.textQuery(JsonUtils.serialize(suggestionQuery));
            return new MetricSuggestionResult(score.getName(), chatQueryResult.getAnswer());
        } catch (Exception e) {
            log.error("指标ai分析失败:", e);
        }
        return new MetricSuggestionResult(score.getName(), StringUtils.EMPTY_STRING);
    }
}
