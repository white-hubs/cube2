package com.guhecloud.cube.biz.fusion.communicate.server.conference.domain.enumeration;

import lombok.Getter;

@Getter
public enum DepartureTypeEnum {

    /**
     * 主动退出
     */
    PROACTIVELY_WITHDRAW,

    /**
     * 踢出
     */
    KICK_OUT,

    /**
     * 房间关闭
     */
    ROOM_CLOSE;
}
