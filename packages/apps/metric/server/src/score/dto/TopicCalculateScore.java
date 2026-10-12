package com.guhecloud.cube.biz.metric.server.score.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 计算项明细
 *
 * @author lanchun
 * @date 2025/6/3 17:42
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TopicCalculateScore {

    /**
     * 一次报告计算的请求id
     */
    private String requestId;

    /**
     * 主题
     */
    private String topic;

    /**
     * 主题concat
     */
    private String topicConcat;

    /**
     * 主题名称
     */
    private String topicName;

    /**
     * 计算开始时间
     */
    private LocalDateTime beginTime;

    /**
     * 计算结束时间
     */
    private LocalDateTime endTime;

    /**
     * 得分
     */
    private Double score;

    /**
     * 权重得分
     */
    private Double weightScore;

    /**
     * 计算结果
     */
    private Double result;

    /**
     * 指标结果
     */
    private Object metricResult;

    /**
     * 建议
     */
    private String suggestion;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 内容模版
     */
    private String contentTemplate;

    /**
     * 权重
     */
    private Double weightAllot;

    /**
     * 内容变量
     */
    private Map<String, Object> contentVariables;

    /**
     * 子级项得分
     */
    private List<TopicCalculateScore> subTopicCalculateScores;
}
