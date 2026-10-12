package com.guhecloud.subsystem.requester.sdk.param.drone;

import lombok.Data;

@Data
public class DroneTakeOffParam {
    /**
     * 机库编号（机巢SN）。
     */
    private String nestSn;
    /**
     * 手动任务调度ID（scheduleId），由前端传入。
     */
    private Long scheduleId;
}
