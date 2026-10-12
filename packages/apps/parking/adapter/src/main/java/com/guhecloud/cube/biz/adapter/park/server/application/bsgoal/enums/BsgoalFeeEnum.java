package com.guhecloud.cube.biz.adapter.park.server.application.bsgoal.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BsgoalFeeEnum {

    CASH_PAY(0, "现金"),

    ALI_PAY(1, "支付宝"),

    WE_CHAT_PAY(2, "微信"),

    UNION_PAY(3, "银联"),

    OTHER_PAY(4, "其他"),
    ;

    private final Integer code;
    private final String name;

}
