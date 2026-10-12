package com.guhecloud.cube.biz.metric.server.score.suggestion.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class MetricSuggestionQuery {

    /**
     * 指标
     */
    private String metric;

    /**
     * 指标数据
     */
    private List<MetricDate> dates;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MetricDate {

        /**
         * 开始时间
         */
        private LocalDateTime beginTime;

        /**
         * 结束时间
         */
        private LocalDateTime endTime;

        /**
         * 数据
         */
        private Object data;
    }
}
