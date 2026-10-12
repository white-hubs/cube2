package com.guhecloud.cube.srv.gen.adapter.onecard.server.enumeration;

import lombok.Getter;

/**
 * 结合余额类型
 *
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/4/18 18:13
 * @Description:
 */
@Getter
public enum CombinationTypeEnum {
    SUBSIDIES_CASH(1, "补贴+现金"),
    HAIRCUT_COUPONS(2, "理发券"),
    RESERVED_AMOUNT(3, "预留金额"),
    ;
    private final Integer code;
    private final String desc;

    CombinationTypeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static String getEnumDesc(Integer code) {
        for (CombinationTypeEnum value : values()) {
            if (value.getCode().equals(code)) {
                return value.getDesc();
            }
        }
        return null;
    }

}
