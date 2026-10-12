package com.guhecloud.cube.biz.metric.server.score.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class Score {

    /**
     * 一次报告计算的请求id
     */
    private String requestId;

    /**
     * 报告名称
     */
    private String name;

    /**
     * 计算开始时间
     */
    private LocalDateTime beginTime;

    /**
     * 计算结束时间
     */
    private LocalDateTime endTime;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 得分
     */
    private Double score;

    /**
     * 建议
     */
    private String suggestion;

    /**
     * 记分项列表
     */
    private List<TopicCalculateScore> items;
}
