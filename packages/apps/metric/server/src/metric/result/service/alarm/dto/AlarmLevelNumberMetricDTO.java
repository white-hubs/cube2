package com.guhecloud.cube.biz.metric.server.metric.result.service.alarm.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 报警级别数量指标
 *
 * @author lanchun
 * @date 2025/6/4 20:05
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AlarmLevelNumberMetricDTO {

    /**
     * 报警级别编号
     */
    private String code;

    /**
     * 报警级别名称
     */
    private String name;

    /**
     * 报警数量
     */
    private Long alarmNum;

    /**
     * 占比
     */
    private Double ratio;
}
