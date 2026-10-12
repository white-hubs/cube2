package com.guhecloud.cube.biz.subsystem.parking.server.parking.interfaces.vo;

import lombok.Data;

/**
 * 停车时长返回参数
 *
 * @author lanchun
 * @date 2022/12/23 14:18
 */
@Data
public class ParkingDurationDistributionVO {

    /**
     * 数量
     */
    private Integer num;

    /**
     * 时间点
     */
    private String period;
}
