package com.guhecloud.cube.srv.gen.conferencemanage.server.enumeration;

import lombok.Getter;

@Getter
public enum BookRoomEnum {
    RESERVED("已预约"),
    APPROVAL("预约中"),
    RESERVABLE("空闲中"),
    BEING_USED("使用中"),
    EXPIRE("不可用"),
    CANCEL_RESERVATION("取消"),
    FINISH("已结束");
    /**
     * 描述信息
     */
    private final String description;

    BookRoomEnum(String description) {
        this.description = description;
    }
}
