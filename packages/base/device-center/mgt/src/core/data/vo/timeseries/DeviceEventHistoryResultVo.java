package com.guhecloud.cube.iot.mgt.core.data.vo.timeseries;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DeviceEventHistoryResultVo {
    private Long deviceId;
    private String eventCode;
    private String instanceId;
    private String category;
    private String bizType;
    private LocalDateTime eventTime;
    private String eventTimeStr;
}
