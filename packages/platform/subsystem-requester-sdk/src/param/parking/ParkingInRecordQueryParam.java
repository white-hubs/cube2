package com.guhecloud.subsystem.requester.sdk.param.parking;

import lombok.Data;

@Data
public class ParkingInRecordQueryParam {

    /**
     * 车牌号
     */
    private String vehicleNo;

    /**
     * 入场开始时间
     */
    private String inBeginTime;

    /**
     * 入场结束时间
     */
    private String inEndTime;
}
