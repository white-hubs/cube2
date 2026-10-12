package com.guhecloud.cube.biz.adapter.park.server.application.jscloud.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.guhecloud.cube.biz.adapter.park.server.application.jscloud.param.JsCloudBwListBizContent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 黑白灰名单接口请求体，严格对应文档
 * app_id, method, sign_type, timestamp, charset, format, projectCode, abilityCode, sign, biz_content
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JsCloudBwListApiRequest {

    @JsonProperty("app_id")
    private String appId;

    private String method;

    @JsonProperty("sign_type")
    private String signType;

    private Long timestamp;
    private String charset;
    private String format;

    private String projectCode;
    private String abilityCode;

    private String sign;

    @JsonProperty("biz_content")
    private JsCloudBwListBizContent bizContent;
}
