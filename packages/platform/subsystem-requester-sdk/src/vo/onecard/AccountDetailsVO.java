package com.guhecloud.subsystem.requester.sdk.vo.onecard;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/4/18 19:18
 * @Description:
 */
@Data
public class AccountDetailsVO {

    /**
     * 主键id
     */
    private Long id;

    /**
     * 卡号
     */
    private String userCard;

    /**
     * 补贴余额
     */
    private BigDecimal subsidyBalance;

    /**
     * 现金余额
     */
    private BigDecimal cashBalance;

    /**
     * 理发卷金额
     */
    private BigDecimal haircutCount;

    /**
     * 洗衣次数
     */
    private Integer laundryCount;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
