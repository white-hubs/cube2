package com.guhecloud.cube.biz.adapter.park.server.param;

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
