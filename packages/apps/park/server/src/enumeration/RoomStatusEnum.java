package com.guhecloud.cube.srv.gen.park.management.server.enumeration;

import lombok.Getter;

/**
 * 房间状态
 */
@Getter
public enum RoomStatusEnum {

    /**
     * 正常
     */
    NORMAL("正常"),

    /**
     * 停用
     */
    STOPPING("停用"),

    ;

    /**
     * 描述信息
     */
    private final String description;

    RoomStatusEnum(String description) {
        this.description = description;
    }
}
