package com.guhecloud.cube.biz.meattrade.server.wallet.enumeration;

import lombok.Getter;
import lombok.NonNull;

/**
 * 业务类型枚举
 *
 * @author Woody
 * @date 2021/9/14
 */
@Getter
public enum BusinessType {
    /**
     * 账户充值
     */
    DEPOSIT("账户充值"),

    /**
     * 账户提现
     */
    WITHDRAW("账户提现"),

    /**
     * 佣金分成
     */
    COMMISSION("佣金分成"),

    /**
     * 肉类交易
     */
    PORK_TRADE("肉类交易"),

    /**
     * 合同缴费单
     */
    PAYMENT_NOTICE("合同缴费单"),

    /**
     * 商城订单
     */
    ENTER_CHARGE("进门收费"),

    /**
     * 商城订单
     */
    MALL_ORDER("商城订单");

    /**
     * 描述信息
     */
    private final String description;

    BusinessType(@NonNull String description) {
        this.description = description;
    }
}
