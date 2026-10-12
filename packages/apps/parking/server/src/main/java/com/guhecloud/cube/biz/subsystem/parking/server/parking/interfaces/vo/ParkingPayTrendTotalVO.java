package com.guhecloud.cube.biz.subsystem.parking.server.parking.interfaces.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 车辆缴费趋势图
 *
 * @author lanchun
 * @date 2022/10/13 14:37
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ParkingPayTrendTotalVO {

    /**
     * 金额
     */
    private BigDecimal amount;

    /**
     * 时间点
     */
    private String period;
}
