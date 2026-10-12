package com.guhecloud.subsystem.requester.sdk.vo.information;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 发送节目到终端的返回值
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SendProgramToTerminalVo {
    /**
     * 发送 id，查询发送结果，或者取消的时候需要传入这个
     */
    private String id;

    /**
     * 第三方终端id
     */
    private String terminalId;

    /**
     * 发送状态 0:成功，1：失败
     */
    private Integer sendStatus;

    /**
     * 发送结果
     */
    private String sendResult;
}
