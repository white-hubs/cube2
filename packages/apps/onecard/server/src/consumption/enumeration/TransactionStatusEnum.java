package com.guhecloud.cube.onecard.server.consumption.enumeration;

import lombok.Getter;

@Getter
public enum TransactionStatusEnum {

    SUCCESS(1, "成功"),
    FAILD(0, "失败");

    private Integer code;
    private String value;

    TransactionStatusEnum(Integer code, String value) {
        this.code = code;
        this.value = value;
    }

    public static String getValue(Integer code) {
        TransactionStatusEnum[] transactionChannelEnums = values();
        for (TransactionStatusEnum transactionChannelEnum : transactionChannelEnums) {
            if (transactionChannelEnum.getCode().equals(code)) {
                return transactionChannelEnum.getValue();
            }
        }
        return null;
    }
}
