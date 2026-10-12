package com.guhecloud.cube.srv.gen.adapter.access.control.server.model.chean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheanResponse<T> {
    //请求是否成功
    private Boolean status;
    //状态码
    private int code;
    //描述
    private String message;
    //数据
    private T data;
}
