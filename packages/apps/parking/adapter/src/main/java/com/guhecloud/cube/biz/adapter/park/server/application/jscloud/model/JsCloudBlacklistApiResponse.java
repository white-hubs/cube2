package com.guhecloud.cube.biz.adapter.park.server.application.jscloud.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 捷顺云开放平台 - 黑名单接口响应
 * 响应码 000000 表示成功
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JsCloudBlacklistApiResponse {

    /**
     * 响应码，000000-成功
     */
    private String code;

    /**
     * 响应消息
     */
    private String message;

    /**
     * 响应数据
     */
    private Object data;
}
