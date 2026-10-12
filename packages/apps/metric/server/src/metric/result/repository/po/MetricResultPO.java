package com.guhecloud.cube.biz.metric.server.metric.result.repository.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 指标结果
 *
 * @author lanchun
 * @date 2025/6/4 15:47
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName(value = "metric_result")
public class MetricResultPO extends BasicEnhancedTenantEntity<Long> {

    private Long id;

    /**
     * 请求id
     */
    private String requestId;

    /**
     * 应用编号
     */
    private String appCode;

    /**
     * 计算开始时间
     */
    private LocalDateTime beginTime;

    /**
     * 计算结束时间
     */
    private LocalDateTime endTime;

    /**
     * 指标主题
     */
    private String metric;

    /**
     * 指标结果
     */
    private String data;
}
