package com.guhecloud.cube.srv.gen.securitymanagement.server.emergencyphonenumber.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EmergencyPhoneNumberVo {
    private Long id;
    private String recordId;
    private LocalDateTime dataTime;
    private String deviceId;
    private String fileUrl;
    private String timeFlag;
    private String deviceCode;
    private String areaName;
    private int num;
}
