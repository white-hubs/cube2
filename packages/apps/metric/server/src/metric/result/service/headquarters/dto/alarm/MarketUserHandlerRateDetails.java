package com.guhecloud.cube.biz.metric.server.metric.result.service.headquarters.dto.alarm;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/6/13 17:34
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MarketUserHandlerRateDetails {

    /**
     * 用户所在市场
     */
    private String userMarketName;

    /**
     * 用户处理报警数
     */
    private Integer userHandlerAlarmNumber;

    /**
     * 用户名称
     */
    private String alarmHandlerUserName;

    /**
     * 用户处理率
     */
    private Double userHandlerRate;
}
