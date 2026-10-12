package com.guhecloud.cube.onecard.server.consumption.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/4/18 18:44
 * @Description:
 */
@Data
public class AccountDetailsVO2 {

    /**
     * 主键id
     */
    private Long id;
    /**
     * 姓名
     */
    private String userName;
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
     * 消费卡号
     */
    private String cardNo;

}
