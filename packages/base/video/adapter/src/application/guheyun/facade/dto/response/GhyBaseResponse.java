package com.guhecloud.cube.srv.gen.adapter.video.server.application.guheyun.facade.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class GhyBaseResponse<T> {

    /**
     * 错误码，0为成功
     */
    private Integer code;

    /**
     * 描述，错误时描述错误原因
     */
    private String msg;

    /**
     * 数据体
     */
    private T data;
}
