package com.guhecloud.cube.base.cloud.qr.code.server.domain.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class QrCode {

    private Long id;
    private String code;
    private String name;
    private String shortCode;
    private Long qrTypeId;
    private String qrContentType;
    private String qrContent;
    private String mode;
    private String modeContent;
    private String grantClientId;
    private QrRoute qrRoute;
    private Boolean enabled;
    private LocalDateTime createdTime;
    private String createdBy;
    private LocalDateTime updatedTime;
    private String updatedBy;
}
