package com.guhecloud.cube.srv.gen.adapter.access.control.server.model.dahua.api.vo;

import lombok.Data;

/**
 * 大华分页接口请求返回
 */
@Data
public class DaHuaResponse {

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
    private Object data;
}
