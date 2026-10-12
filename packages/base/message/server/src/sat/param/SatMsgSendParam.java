package com.guhecloud.cube.base.message2.server.sat.param;

import lombok.Data;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.Map;

/**
 * 平台消息发送参数
 *
 * @author lanchun
 * @date 2021/11/15 18:47
 */
@Data
public class SatMsgSendParam {

    /**
     * 消息场景编号
     */
    private String msgSceneCode;

    /**
     * 业务主键
     */
    @Size(max = 50, message = "业务主键不能大于50")
    private String businessId;

    /**
     * 推送的业务主体
     */
    private Object businessPrincipal;

    /**
     * 消息参数不能为空
     */
    private Map<String, String> msgParams;

    /**
     * 发送通道
     */
    @NotNull(message = "发送通道不能为空")
    @Size(min = 1, message = "发送通道不能为空")
    private List<SatMsgChannelSendParam> satMsgChannelSendParams;

    /**
     * 应用编号
     */
    private String applicationCode;

    /**
     * 应用名称
     */
    private String applicationName;

    /**
     * 租户编号
     */
    private Integer tenant;

    /**
     * 来源租户
     */
    private Integer sourceTenant;
}
