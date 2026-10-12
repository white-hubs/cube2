package com.guhecloud.cube.biz.subsystem.parking.server.parking.interfaces.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 车辆入场记录趋势图
 *
 * @author lanchun
 * @date 2022/10/13 14:37
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ParkingAccessRecordTrendTotalVO {

    /**
     * 数量
     */
    private Long num;

    /**
     * 时间点
     */
    private String period;
}
