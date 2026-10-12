package com.guhecloud.cube.biz.adapter.park.server.application.jscloud.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 数据中心登录响应
 * resultCode=0 表示成功，tn 为令牌，timeOut 为有效时间(秒)
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JsCloudDataCenterLoginResponse {
    private Integer resultCode;
    private String message;
    private String tn;
    /** 令牌有效时间，单位秒，默认86400(24小时) */
    private Integer timeOut;
}
