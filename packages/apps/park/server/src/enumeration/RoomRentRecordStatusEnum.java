package com.guhecloud.cube.srv.gen.park.management.server.enumeration;

import lombok.Getter;

/**
 * 房间状态
 */
@Getter
public enum RoomRentRecordStatusEnum {

    /**
     * 正常
     */
    NORMAL("正常"),

    /**
     * 过期
     */
    EXPIRE("过期"),

    /**
     * 未生效
     */
    EXECUTING("未生效"),

    ;

    /**
     * 描述信息
     */
    private final String description;

    RoomRentRecordStatusEnum(String description) {
        this.description = description;
    }
}
