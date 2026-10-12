package com.guhecloud.cube.sn.meattrade.plugin.dbc.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class BaseResponse<T> {

    /**
     * 是否成功
     */
    private Boolean success;

    /**
     * code码
     */
    private Integer code;

    /**
     * 描叙
     */
    private String msg;

    /**
     * 数据
     */
    private T data;

    /**
     * 时间戳
     */
    private Long timestamp;

}
