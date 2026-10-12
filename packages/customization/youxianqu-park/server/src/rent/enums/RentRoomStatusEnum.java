package com.guhecloud.cube.customized.youxianqu.park.management.server.rent.enums;

import lombok.Getter;

/**
 * 租控记录房间状态枚举
 */
@Getter
public enum RentRoomStatusEnum {
    IDLE("0", "闲置"),
    RENTED("1", "已租"),
    RESERVE("2", "预定"),
    SOLD("16", "已售"),
    ;
    final String code;
    final String describe;

    RentRoomStatusEnum(String code, String describe) {
        this.code = code;
        this.describe = describe;
    }
}
