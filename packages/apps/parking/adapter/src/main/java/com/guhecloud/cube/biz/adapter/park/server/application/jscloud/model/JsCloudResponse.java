package com.guhecloud.cube.biz.adapter.park.server.application.jscloud.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 捷顺云开放平台 - 车位上报响应
 * 返回值: JSON格式字符串
 *
 * @author appet
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JsCloudResponse {
    /**
     * 返回码，数字，必填，0=成功，1=失败
     */
    private Integer resultCode;
    /**
     * 返回信息，字符，可选
     */
    private String message;
}
