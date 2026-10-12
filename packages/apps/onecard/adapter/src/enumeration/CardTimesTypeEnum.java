package com.guhecloud.cube.srv.gen.adapter.onecard.server.enumeration;

import lombok.Getter;

/**
 * 卡次类型
 *
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/4/18 18:18
 * @Description:
 */
@Getter
public enum CardTimesTypeEnum {
    LAUNDRY_CARD(1, "洗衣卡"),
    RESERVED_CARD_TIMES(2, "预留卡次"),
    ;
    private final Integer code;
    private final String desc;

    CardTimesTypeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static String getEnumDesc(Integer code) {
        for (CardTimesTypeEnum value : values()) {
            if (value.getCode().equals(code)) {
                return value.desc;
            }
        }
        return null;
    }
}
