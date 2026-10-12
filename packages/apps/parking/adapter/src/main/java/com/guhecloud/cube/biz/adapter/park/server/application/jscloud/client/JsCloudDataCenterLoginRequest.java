package com.guhecloud.cube.biz.adapter.park.server.application.jscloud.client;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 数据中心登录请求
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JsCloudDataCenterLoginRequest {
    private String pno;
    private String secret;
}
