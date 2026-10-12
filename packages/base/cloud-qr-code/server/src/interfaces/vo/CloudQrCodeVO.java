package com.guhecloud.cube.base.cloud.qr.code.server.interfaces.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CloudQrCodeVO {

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
    private String grantClientName;
    private CloudQrRouteVO route;
    private Boolean enabled;
    private LocalDateTime updateTime;
}
