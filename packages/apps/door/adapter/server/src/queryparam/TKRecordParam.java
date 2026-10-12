package com.guhecloud.cube.srv.gen.adapter.access.control.server.queryparam;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TKRecordParam {
    //开始时间
    private LocalDateTime startTime;
    //结束时间
    private LocalDateTime endTime;
}
