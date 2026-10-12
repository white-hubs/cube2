package com.guhecloud.cube.biz.adapter.park.server.application.aike.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AiKe2Response {
    private String command;
    private String message_id;
    private String sign_type;
    private String sign;
    private String charset = "UTF-8";
    private String timestamp;//格式：yyyyMMddHHmmss
    @JsonProperty("biz_content")
    private ReportResponse bizContent;

    @Data
    @Builder
    public static class ReportResponse {
        private String code;//0表示成功，非0表示失败
        private String msg; //返回结果消息描述

    }
}
