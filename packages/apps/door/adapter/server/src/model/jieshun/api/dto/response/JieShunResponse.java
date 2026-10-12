package com.guhecloud.cube.srv.gen.adapter.access.control.server.model.jieshun.api.dto.response;

import lombok.Data;

/**
 * author: ht
 * create: 2025-05-22 11:43
 */
@Data
public class JieShunResponse<T> {
    private String code;
    private String message;
    private T data;

    public static <T> JieShunResponse<T> success(T data) {
        JieShunResponse<T> response = new JieShunResponse<>();
        response.setCode("0");
        response.setMessage("成功");
        response.setData(data);
        return response;
    }
    public static  JieShunResponse<Void> success() {
        JieShunResponse<Void> response = new JieShunResponse<>();
        response.setCode("0");
        response.setMessage("成功");
        response.setData(null);
        return response;
    }


    public static JieShunResponse<Void> fail(String message) {
        JieShunResponse<Void> response = new JieShunResponse<>();
        response.setCode("-1");
        response.setMessage(message);
        return response;
    }
}
