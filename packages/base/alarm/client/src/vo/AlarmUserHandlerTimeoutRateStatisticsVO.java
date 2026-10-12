package com.guhecloud.cube.base.alarm.client.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 用户报警处理超时率统计
 *
 * @author lanchun
 * @date 2025/6/20 16:21
 */
@Data
public class AlarmUserHandlerTimeoutRateStatisticsVO {

    /**
     * 用户id
     */
    private String userId;

    /**
     * 用户名称
     */
    private String username;

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
