package com.guhecloud.cube.base.alarm.client.param;

import lombok.Data;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

/**
 * 报警通知再次发送参数
 *
 * @author lanchun
 * @date 2022/6/8 10:27
 */
@Data
public class SatAlarmMsgReSendParam {

    /**
     * 业务主键
     */
    @NotNull(message = "业务主键不能为空")
    private String businessKey;

    /**
     * 发送对象
     */
    @NotNull(message = "发送对象不能为空")
    @Size(min = 1, message = "发送对象不能为空")
    private List<SatAlarmMsgTargetSendParam> sendTargetParams;
}
