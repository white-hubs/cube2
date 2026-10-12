package com.guhecloud.cube.srv.gen.adapter.access.control.server.model.das.api.vo;

import lombok.Data;

/**
 * 大华分页接口请求返回
 */
@Data
public class DasControlResponse {

    /**
     * 错误码：0-为无错误，非0-为具体错误码
     */
    private Integer code;
    /**
     * 错误消息
     */
    private String msg;
    /**
     * 返回数据对象
     */
    private Object data;
}
