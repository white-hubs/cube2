package com.guhecloud.cube.base.message2.client.param;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 站内信返送参数
 *
 * @author lanchun
 * @date 2022/6/7 9:29
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SatMsgSiteEventDataParam {

    private String event;

    @NotNull(message = "发送对象不能为空")
    private Object data;
}
