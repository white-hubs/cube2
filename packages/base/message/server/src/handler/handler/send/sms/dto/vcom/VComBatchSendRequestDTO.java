package com.guhecloud.cube.base.message2.server.handler.handler.send.sms.dto.vcom;

import lombok.Data;

import java.util.List;

@Data
public class VComBatchSendRequestDTO {

    /**
     * 账号
     */
    private String account;

    /**
     * 时间戳
     */
    private Long timestamp;

    /**
     * 随机数
     */
    private Integer nonce;

    /**
     * 签名类型
     */
    private String signType;

    /**
     * 签名
     */
    private String signature;

    /**
     *
     */
    private List<VComSendRequestDTO> params;
}
