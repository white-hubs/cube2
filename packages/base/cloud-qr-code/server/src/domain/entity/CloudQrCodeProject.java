package com.guhecloud.cube.base.cloud.qr.code.server.domain.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 云二维码项目实体
 */
@Data
public class CloudQrCodeProject {

    private Long id;
    private String name;
    private String code;
    private String secret;
    private LocalDateTime createdTime;
    private String createdBy;
    private LocalDateTime updatedTime;
    private String updatedBy;
}
