package com.guhecloud.cube.base.cloud.qr.code.sdk.model;

import lombok.Data;

/**
 * 二维码路由DTO
 *
 * @author system
 */
@Data
public class QrRouteDTO {

    private String targetUrl;
    private String targetParams;
}
