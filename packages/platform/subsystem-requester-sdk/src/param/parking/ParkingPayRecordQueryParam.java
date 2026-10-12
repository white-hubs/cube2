package com.guhecloud.subsystem.requester.sdk.param.parking;

import lombok.Data;

@Data
public class ParkingPayRecordQueryParam {

    /**
     * 车牌号
     */
    private String vehicleNo;

    /**
     * 缴费开始时间
     */
    private String beginTime;

    /**
     * 缴费结束时间
     */
    private String bndTime;
}
