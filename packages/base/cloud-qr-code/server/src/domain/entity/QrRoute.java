package com.guhecloud.cube.base.cloud.qr.code.server.domain.entity;

import lombok.Data;

@Data
public class QrRoute {
    //实际跳转的目标URL
    private String targetUrl;
    //跳转参数（JSON格式）
    private String targetParams;
}
