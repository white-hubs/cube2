package com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.domain.valueobject;

import lombok.Getter;

/**
 * 人员工作状态
 */
@Getter
public enum AttStaffWorkStatusEnum {

    REST("休息"),

    WORKING("在岗"),

    AWAY_BUSINESS("出差"),

    LEAVE_POST("离岗"),

    ASK_LEAVE("请假");

    private final String value;

    AttStaffWorkStatusEnum(String value) {
        this.value = value;
    }
}
