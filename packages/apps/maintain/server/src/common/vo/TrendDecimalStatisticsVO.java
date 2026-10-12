package com.guhecloud.cube.srv.gen.maintain.server.common.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/5/8 9:33
 * @Description:
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrendDecimalStatisticsVO {

    /**
     * 时间点
     */
    private String time;

    /**
     * 值
     */
    private BigDecimal count;

    /**
     * 是否为最高峰
     */
    private Boolean isPeak;
}
