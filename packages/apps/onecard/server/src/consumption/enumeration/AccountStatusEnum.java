package com.guhecloud.cube.onecard.server.consumption.enumeration;

import lombok.Getter;

@Getter
public enum AccountStatusEnum {

    NORMAL("normal", "正常"),
    EXPIRED("expired", "过期");

    private String code;
    private String value;

    AccountStatusEnum(String code, String value) {
        this.code = code;
        this.value = value;
    }

    public String getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }

    public static String getValue(String code) {
        AccountStatusEnum[] accountStatusEnums = values();
        for (AccountStatusEnum accountStatusEnum : accountStatusEnums) {
            if (accountStatusEnum.getCode().equals(code)) {
                return accountStatusEnum.getValue();
            }
        }
        return null;
    }
}
