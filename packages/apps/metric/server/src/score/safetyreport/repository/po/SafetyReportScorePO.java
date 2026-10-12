package com.guhecloud.cube.biz.metric.server.score.safetyreport.repository.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 分析报告得分
 *
 * @author lanchun
 * @date 2025/6/5 11:36
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("safety_report_score")
public class SafetyReportScorePO extends BasicEnhancedTenantEntity<Long> {

    private Long id;

    /**
     * 报告名称
     */
    private String name;

    /**
     * 请求id
     */
    private String requestId;

    /**
     * 计算开始时间
     */
    private LocalDateTime beginTime;

    /**
     * 计算结束时间
     */
    private LocalDateTime endTime;

    /**
     * 分数
     */
    private Double score;

    /**
     * 建议
     */
    private String suggestion;

    /**
     * 报告数据
     */
    private byte[] data;
}
