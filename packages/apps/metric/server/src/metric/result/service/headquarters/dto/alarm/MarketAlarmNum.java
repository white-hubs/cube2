package com.guhecloud.cube.biz.metric.server.metric.result.service.headquarters.dto.alarm;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author lanchun
 * @date 2025/6/16 15:02
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MarketAlarmNum {

    /**
     * 市场名称
     */
    private String marketName;

    /**
     * 报警数
     */
    private Integer alarmNumber;
}
