package com.guhecloud.cube.srv.gen.visitor.management.server.enums;

import lombok.Getter;

/**
 * 预约状态
 */
@Getter
public enum ReservationStatusEnum {
    TO_BE_REVIEWED(0, "待审核"),
    RESERVATION(1, "待使用"),
    ON_THE_VISIT(2, "进行中"),
    DEPARTURE(3, "已使用"),
    CANCELLATION(4, "已取消"),
    ;

    final Integer status;
    final String statusName;

    ReservationStatusEnum(int status, String statusName) {
        this.status = status;
        this.statusName = statusName;
    }
}
