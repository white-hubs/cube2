package com.guhecloud.cube.base.message2.server.handler.handler.send.wecom.dto;

import lombok.Data;

@Data
public class WeComBaseResponse {

    /**
     * 错误码
     */
    private Integer errcode;

    /**
     * 错误消息
     */
    private String errmsg;
}
