package com.guhecloud.cube.srv.gen.official.management.server.dao.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DocumentReceivedStatDTO {
    private Long applicantId;
    private String applicantName;
    private String fromUnit;
    private String fileCode;
    private LocalDateTime applyTime;
    private LocalDateTime applyDoneTime;
    private String timeLimit;
    private String timeout;
    private String timeoutDesc;
}
