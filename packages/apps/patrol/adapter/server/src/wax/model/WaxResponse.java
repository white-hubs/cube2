package com.guhecloud.cube.srv.gen.adapter.patrol.server.wax.model;

import lombok.Data;

@Data
public class WaxResponse<T> {
    private int code;
    private String errorMsg;
    private T data;
}
