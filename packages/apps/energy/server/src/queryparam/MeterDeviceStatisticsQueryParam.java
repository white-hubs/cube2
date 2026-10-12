package com.guhecloud.cube.srv.gen.energymanagement.server.queryparam;

import lombok.Data;

@Data
public class MeterDeviceStatisticsQueryParam {
    private Long startTs;
    private Long endTs;
    private Integer step;
    private Long deviceId;
}
