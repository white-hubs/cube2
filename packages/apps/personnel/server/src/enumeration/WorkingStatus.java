package com.guhecloud.cube.srv.gen.personnel.management.server.enumeration;

import lombok.Getter;

/**
 * 员工工作状态
 */
@Getter
public enum WorkingStatus {
    /**
     * 请假
     */
    ASK_LEAVE("请假"),
    /**
     * 在岗
     */
    WORKING("在岗"),
    /**
     * 离岗
     */
    LEAVE_POST("离岗"),
    /**
     * 休息中
     */
    REST("休息"),

    AWAY_BUSINESS("出差");

    final String value;

    WorkingStatus(String value) {
        this.value = value;
    }

}
