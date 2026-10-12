package com.guhecloud.subsystem.requester.sdk.param.accesscontrol;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class AccessEventParam {
    //开始时间
    private LocalDateTime startTime;
    //结束时间
    private LocalDateTime endTime;
    //页码
    private int pageNo;
    //页大小
    private int pageSize;
}
