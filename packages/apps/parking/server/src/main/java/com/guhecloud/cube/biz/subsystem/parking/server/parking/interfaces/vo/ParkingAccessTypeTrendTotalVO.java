package com.guhecloud.cube.biz.subsystem.parking.server.parking.interfaces.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 车辆通行类型趋势图
 *
 * @author lanchun
 * @date 2022/10/13 16:00
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParkingAccessTypeTrendTotalVO {

    /**
     * 入场趋势
     */
    private List<ParkingAccessRecordTrendTotalVO> inTrendVOS;

    /**
     * 出场趋势
     */
    private List<ParkingAccessRecordTrendTotalVO> outTrendVOS;
}
