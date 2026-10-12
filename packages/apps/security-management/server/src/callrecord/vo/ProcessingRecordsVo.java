package com.guhecloud.cube.srv.gen.securitymanagement.server.callrecord.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ProcessingRecordsVo {
    private Long id;
    private String callId;
    private String processedBy;
    private LocalDateTime processedTime;
    private String processedResult;
}
