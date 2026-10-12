package com.guhecloud.cube.srv.gen.securitymanagement.server.information.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PlayRecordPageVo {
    private Long id;

    private String recordId;

    private String clientId;
    private String clientName;

    private String itemName;

    private LocalDateTime startTime;

    private LocalDateTime stopTime;

    private int totalTime;

    private int totalCircle;

    private String areaId;
    private String areaName;
}
