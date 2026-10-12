package com.guhecloud.cube.srv.gen.securitymanagement.server.medicalgas.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class MedicalGasDataPageVo {
    private Long id;
    private String name;
    private String gasType;
    private LocalDateTime collectionTime;
    private BigDecimal dataValue;
    private String thirdDeviceId;
    private String deviceName;
    private String areaName;
}
