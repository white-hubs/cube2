package com.guhecloud.cube.base.cloud.qr.code.server.interfaces.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 云二维码项目VO
 */
@Data
public class CloudQrCodeProjectVO {

    private Long id;
    private String name;
    private String code;
    private String secret;
    private LocalDateTime updateTime;
}
