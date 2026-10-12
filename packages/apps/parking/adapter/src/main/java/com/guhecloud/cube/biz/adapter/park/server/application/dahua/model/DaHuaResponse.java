package com.guhecloud.cube.biz.adapter.park.server.application.dahua.model;

import lombok.Data;

@Data
public class DaHuaResponse {
    private int code;
    private Boolean success;
    private String errorMsg;
    private Integer isEncrypt;
    private Object data;
}
