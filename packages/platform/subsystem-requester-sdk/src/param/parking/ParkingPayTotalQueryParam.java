package com.guhecloud.subsystem.requester.sdk.param.parking;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ParkingPayTotalQueryParam {

    /**
     * 开始时间
     */
    private LocalDateTime beginTime;

    /**
     * 结束时间
     */
    private LocalDateTime endTime;
}
