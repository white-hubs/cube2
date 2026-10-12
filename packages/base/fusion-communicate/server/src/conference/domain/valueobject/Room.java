package com.guhecloud.cube.biz.fusion.communicate.server.conference.domain.valueobject;

import lombok.Getter;

/**
 * 房间信息
 *
 * @author lanchun
 * @date 2025/2/11 17:43
 */
@Getter
public class Room {

    /**
     * 房间号
     */
    private final String number;

    public Room(String number) {this.number = number;}
}
