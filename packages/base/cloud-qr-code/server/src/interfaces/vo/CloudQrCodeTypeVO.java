package com.guhecloud.cube.base.cloud.qr.code.server.interfaces.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CloudQrCodeTypeVO {

    private Long id;
    private String code;
    private String name;
    private String remark;
    private String grantClientId;
    private String grantClientName;
    private LocalDateTime updateTime;
}
