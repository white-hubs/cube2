package com.guhecloud.cube.srv.gen.securitymanagement.server.callrecord.vo;

import com.guhecloud.cube.srv.gen.securitymanagement.server.callrecord.enumeration.CallTypeEnum;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CallRecordVo {
    private Long id;
    private LocalDateTime callTime;
    private String deviceId;
    private String deviceName;
    private String areaName;
    private CallTypeEnum callType;
    private Integer callDuration;
    private String recordId;
    private String bed;
}
