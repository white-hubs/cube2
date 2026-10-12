package com.guhecloud.cube.srv.gen.adapter.onecard.server.enumeration;

import lombok.Getter;

/**
 * 现金类型
 *
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/4/18 18:04
 */
@Getter
public enum CashTypeEnum {
    CASH(1, "现金"),
    SUBSIDY(2, "补贴"),
    HAIRCUT_COUPONS(3, "理发券"),
    RESERVED_AMOUNT(4, "预留金额"),
    ;
    private final Integer code;
    private final String desc;

    CashTypeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static String getEnumDesc(Integer code) {
        for (CashTypeEnum value : values()) {
            if (value.getCode().equals(code)) {
                return value.desc;
            }
        }
        return null;
    }
}
