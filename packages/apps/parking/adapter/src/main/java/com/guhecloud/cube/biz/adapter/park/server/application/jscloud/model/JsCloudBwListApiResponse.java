package com.guhecloud.cube.biz.adapter.park.server.application.jscloud.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 黑白灰名单接口响应，严格对应文档
 * request_id, code, msg, biz_response(respCode, respMsg, success)
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JsCloudBwListApiResponse {

    @JsonProperty("request_id")
    private String requestId;

    private String code;
    private String msg;

    @JsonProperty("biz_response")
    private BizResponse bizResponse;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class BizResponse {
        @JsonProperty("respCode")
        private String respCode;

        @JsonProperty("respMsg")
        private String respMsg;

        private Boolean success;
    }
}
