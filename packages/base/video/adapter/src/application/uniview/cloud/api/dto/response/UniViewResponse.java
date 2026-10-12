package com.guhecloud.cube.srv.gen.adapter.video.server.application.uniview.cloud.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UniViewResponse<T> {

    /**
     * 请求码
     */
    private Integer code;

    /**
     * 请求描述
     */
    private String message;

    /**
     * 返回数据
     */
    private T data;
}
