package com.guhecloud.cube.biz.collection.analysis.gm.model.dto;

import lombok.Data;

/**
 * 光明三方统一响应
 */
@Data
public class GmThirdResponseDTO<T> {
    private Integer code;
    private String msg;
    private T data;
}
