package com.guhecloud.cube.base.alarm.client.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 报警处理超时率统计
 *
 * @author lanchun
 * @date 2025/6/20 16:21
 */
@Data
public class AlarmHandlerTimeoutRateStatisticsVO {

    /**
     * 报警总处理数
     */
    private Integer totalHandlerNum;

    /**
     * 报警总处理超时数量
     */
    private Integer totalHandlerTimeoutNum;

    /**
     * 报警总处理超时率
     */
    private BigDecimal totalHandlerTimeoutRate;
}
