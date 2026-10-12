package com.guhecloud.cube.biz.adapter.park.server.param;

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
