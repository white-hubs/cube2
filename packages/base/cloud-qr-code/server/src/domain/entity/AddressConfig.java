package com.guhecloud.cube.base.cloud.qr.code.server.domain.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 云地址配置实体
 */
@Data
public class AddressConfig {

    private Long id;

    /**
     * 域名地址
     */
    private String ipAddress;

    private LocalDateTime createdTime;

    private String createdBy;

    private LocalDateTime updatedTime;

    private String updatedBy;
}
