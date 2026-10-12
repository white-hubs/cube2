package com.guhecloud.cube.srv.gen.conferencemanage.server.enumeration;

import lombok.Getter;

@Getter
public enum OperateEnum {
    APPOINTMENT("已预约"),
    APPOINTMENT_IN("预约中"),
    CANCEL_RESERVATION("已取消");
    private final String description;

    OperateEnum(String description) {
        this.description = description;
    }
}
