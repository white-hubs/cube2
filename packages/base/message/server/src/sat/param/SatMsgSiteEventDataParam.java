package com.guhecloud.cube.base.message2.server.sat.param;

import lombok.Data;

import jakarta.validation.constraints.NotNull;

/**
 * 站内信返送参数
 *
 * @author lanchun
 * @date 2022/6/7 9:29
 */
@Data
public class SatMsgSiteEventDataParam {

    private String event;

    @NotNull(message = "发送对象不能为空")
    private Object data;
}
