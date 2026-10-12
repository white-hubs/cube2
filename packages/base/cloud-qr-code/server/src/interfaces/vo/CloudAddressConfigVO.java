package com.guhecloud.cube.base.cloud.qr.code.server.interfaces.vo;

import lombok.Data;

/**
 * 云地址配置VO
 */
@Data
public class CloudAddressConfigVO {

    private Long id;

    /**
     * 域名地址
     */
    private String ipAddress;
}
