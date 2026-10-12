package com.guhecloud.subsystem.requester.sdk.vo.passenger;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PassengerVo {
    private String recordId;
    private String areaName;
    private String recordDevice;
    private LocalDateTime recordTime;
    private String inOut;
    private int recordNum;
}
