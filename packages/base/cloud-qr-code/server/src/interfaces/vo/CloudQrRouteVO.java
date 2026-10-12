package com.guhecloud.cube.base.cloud.qr.code.server.interfaces.vo;

import lombok.Data;

@Data
public class CloudQrRouteVO {
    //实际跳转的目标URL
    private String targetUrl;
    //跳转参数（JSON格式）
    private String targetParams;
}
