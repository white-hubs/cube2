package com.guhecloud.cube.base.message2.client.param;

import lombok.Builder;
import lombok.Data;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

/**
 * 站内信返送参数
 *
 * @author lanchun
 * @date 2022/6/7 9:29
 */
@Builder
@Data
public class SatMsgSiteSendParam {

    /**
     * 消息类型
     */
    @NotNull(message = "消息类型不能为空")
    private String msgType;

    /**
     * 消息内容
     */
    @Valid
    @NotNull(message = "消息内容不能为空")
    private SatMsgSiteEventDataParam eventDataParam;

    /**
     * 发送对象
     */
    @Valid
    @NotNull(message = "发送对象不能为空")
    @Size(min = 1, message = "发送对象不能小于1")
    private List<SatMsgSiteTargetModifyParam> targetModifyParams;
}
