package com.guhecloud.cube.biz.linkage.server.record.reconfirmation.domain.enumeration;

import lombok.Getter;

@Getter
public enum AiReconfirmResultEnum {

    /**
     * 确认告警
     */
    CONFIRM_ALARM("确认告警"),

    /**
     * 疑似告警
     */
    SUSPECTED_ALARM("疑似告警");

    private final String desc;

    AiReconfirmResultEnum(String desc) {this.desc = desc;}
}
