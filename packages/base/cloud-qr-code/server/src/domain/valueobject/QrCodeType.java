package com.guhecloud.cube.base.cloud.qr.code.server.domain.valueobject;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class QrCodeType {

    private Long id;
    private String code;
    private String name;
    private String grantClientId;
    private String grantClientName;
    private String remark;
    private LocalDateTime createdTime;
    private String createdBy;
    private LocalDateTime updatedTime;
    private String updatedBy;
}