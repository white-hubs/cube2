package com.guhecloud.cube.biz.metric.server.score.safetyreport.repository.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 规则分数
 *
 * @author lanchun
 * @date 2025/6/4 17:16
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("rule_score")
public class RuleScorePO extends BasicEnhancedTenantEntity<Long> {

    private Long id;

    /**
     * 主题
     */
    private String topic;

    /**
     * 主题concat
     */
    private String topicConcat;

    /**
     * 计算开始时间
     */
    private LocalDateTime beginTime;

    /**
     * 计算结束时间
     */
    private LocalDateTime endTime;

    /**
     * 请求id
     */
    private String requestId;

    /**
     * 分数
     */
    private Double score;

    /**
     * 权重得分
     */
    private Double weightScore;

    /**
     * 建议
     */
    private String suggestion;

    /**
     * 指标项详情
     */
    private byte[] data;
}
