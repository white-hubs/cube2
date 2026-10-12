package com.guhecloud.cube.onecard.server.consumption.enumeration;

import lombok.Getter;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/4/18 14:46
 * @Description:
 */

@Getter
public enum RiXinDaInterfaceEnum {
    CONSUMER_RECORD("2", "消费记录"),
    CONSUMER_TIMES_RECORD("3", "消次记录"),
    REFUND_RECORD("4", "退款记录"),
    REFUND_TIMES_RECORD("5", "退次记录"),
    RECHARGE_RECORD("6", "充减值记录"),
    RECHARGE_TIMES_RECORD("7", "充减次记录"),
    ;

    private final String url;

    private final String desc;

    RiXinDaInterfaceEnum(String url, String desc) {
        this.url = url;
        this.desc = desc;
    }

}
