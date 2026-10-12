package com.guhecloud.cube.srv.gen.adapter.video.server.application.hikvision.isc.facade.dto.response;

import lombok.Data;

@Data
public class BasicResponse<T> {

    /**
     * 状态码
     */
    private String code;

    /**
     * 响应消息
     */
    private String msg;

    /**
     * 响应体
     */
    private T data;

    public boolean succeed() {
        return "0".equals(this.code);
    }
}
