package com.guhecloud.cube.biz.subsystem.parking.server.vehicle.vo.statistics.vehiclebottom;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 车底检测来访趋势图统计
 *
 * @author lanchun
 * @date 2024/12/12 16:53
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReasonTrendTotalVO {

    /**
     * 数量
     */
    private Long num;

    /**
     * 时间点
     */
    private String period;
}
