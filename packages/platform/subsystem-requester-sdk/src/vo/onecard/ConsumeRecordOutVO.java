package com.guhecloud.subsystem.requester.sdk.vo.onecard;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/4/18 15:15
 * @Description:
 */
@Data
public class ConsumeRecordOutVO {

    /**
     * 系统编号
     */
    private Long id;

    /**
     * 记录类型
     */
    private String recordType;
    /**
     * 流水号
     */
    private String serialNumber;

    /**
     * 物理卡号
     */
    private String cardNumber;

    /**
     * 账户ID
     */
    private Long accountId;

    /**
     * 账户类型名称
     */
    private String type;

    /**
     * 消费类型
     */
    private String mode;

    /**
     * 设备名称
     */
    private String device;

    /**
     * 交易时间
     */
    private LocalDateTime tradeTime;

    /**
     * 交易金额
     */
    private BigDecimal tradeAmount;

    /**
     * 补贴金额
     */
    private BigDecimal subsidyAmount;

    /**
     * 交易次数
     */
    private Long times;
}
