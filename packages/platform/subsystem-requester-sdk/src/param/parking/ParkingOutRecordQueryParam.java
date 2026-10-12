package com.guhecloud.subsystem.requester.sdk.param.parking;

import lombok.Data;

@Data
public class ParkingOutRecordQueryParam {

    /**
     * 车牌号
     */
    private String vehicleNo;

    /**
     * 出场开始时间
     */
    private String outBeginTime;

    /**
     * 出场结束时间
     */
    private String outEndTime;
}
