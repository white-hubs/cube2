package com.guhecloud.cube.srv.gen.adapter.onecard.server.dto.cbd.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class BaseResponse<T> {
    private Integer code;
    private Boolean success;
    private T data;
    private String msg;
}
