package com.guhecloud.cube.srv.gen.adapter.access.control.server.model.hik;

import lombok.Data;

@Data
public class HikResponse<T> {
    private String code;
    private String msg;
    private T data;
}
