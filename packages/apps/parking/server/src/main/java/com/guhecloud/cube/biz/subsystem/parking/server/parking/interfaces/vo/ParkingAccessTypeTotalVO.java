package com.guhecloud.cube.biz.subsystem.parking.server.parking.interfaces.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 车辆出入统计
 *
 * @author lanchun
 * @date 2022/10/13 16:01
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ParkingAccessTypeTotalVO {

    /**
     * 入数量
     */
    private Integer inNum;

    /**
     * 出数量
     */
    private Integer outNum;
}
