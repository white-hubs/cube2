package com.guhecloud.cube.srv.gen.adapter.video.server.application.dahua.icc.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 大华分页接口请求返回
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DaHuaResponse<T> {

    /**
     * 接口返回是否成功
     */
    private Boolean success;
    /**
     * 错误码：0-为无错误，非0-为具体错误码
     */
    private String code;
    /**
     * 错误消息
     */
    private String errMsg;
    /**
     * 返回数据对象
     */
    private T data;
}
