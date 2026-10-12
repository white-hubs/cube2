package com.guhecloud.cube.onecard.server.consumption.enumeration;

import lombok.Getter;

@Getter
public enum TransactionChannelEnum {

    ADMINRECHARGE(0, "管理员充值"),
    WEIXINPAY(1, "微信支付"),
    ALIPAY(2, "支付宝支付"),
    BANKCARD(3, "银行卡支付"),
    CASH(4, "现金支付"),
    OTHER(5, "其他");

    private Integer code;
    private String value;

    TransactionChannelEnum(Integer code, String value) {
        this.code = code;
        this.value = value;
    }

    public static String getValue(Integer code) {
        TransactionChannelEnum[] transactionChannelEnums = values();
        for (TransactionChannelEnum transactionChannelEnum : transactionChannelEnums) {
            if (transactionChannelEnum.getCode().equals(code)) {
                return transactionChannelEnum.getValue();
            }
        }
        return null;
    }
}
