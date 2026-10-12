package com.guhecloud.cube.biz.subsystem.parking.server.Illegal.application.commandgetway.command;

import com.guhecloud.cube.biz.subsystem.parking.server.Illegal.domain.enumeration.IllegalLevelEnum;
import com.guhecloud.cube.biz.subsystem.parking.server.Illegal.domain.po.IllegalParkingRecordPO;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2026/1/6 14:18
 * @Description:
 */
@Data
public class VehiclePenaltyCommand {
    /**
     * 违停记录信息
     */
    private IllegalParkingRecordPO illegalParkingRecord;
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
     * 违停程度
     */
    private IllegalLevelEnum illegalLevel;

    /**
     * 违停时间
     */
    private LocalDateTime illegalTime;

}
