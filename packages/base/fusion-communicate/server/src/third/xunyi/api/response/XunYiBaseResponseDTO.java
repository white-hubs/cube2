package com.guhecloud.cube.biz.fusion.communicate.server.third.xunyi.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 讯一调度台的 相关返回数据
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class XunYiBaseResponseDTO {

    private static final long serialVersionUID = 1L;

    private Integer state;
    private Integer code;
    private String describe;
    private String msg;

}
