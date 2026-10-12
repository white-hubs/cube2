package com.guhecloud.cube.biz.subsystem.parking.server.Illegal.domain.aggregates;

import lombok.Data;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2026/1/6 10:59
 * @Description:
 */
@Data
public class IllegalParkingBlacklistInfo {
    /**
     * 车牌号
     */
    private String plateNumber;
    /**
     * 车牌颜色
     */
    private String plateColor;

    /**
     * 车场编号
     */
    private String parkingLot;
    /**
     * 车主
     */
    private String owner;

    /**
     * 联系电话
     */
    private String phone;

    /**
     * 加入或移除
     */
    private Boolean type;

    /**
     * 开始时间
     */
    private String startTime;

    /**
     * 结束时间
     */
    private String endTime;
}
