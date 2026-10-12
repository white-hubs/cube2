package com.guhecloud.cube.base.flow.flowable.process.application.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ProcessInstanceDTO {

    private String id;
    private String key;
    private String name;
    private Integer version;
    private String tenantId;

    private String businessType;
    private String businessKey;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime startTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime endTime;
    private String status;
    private String deleteReason;

}
