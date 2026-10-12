package com.guhecloud.cube.srv.gen.library.management.server.enums;

import lombok.Getter;

/**
 * 文献状态
 */
@Getter
public enum BookStateEnum {
    WAIT_TIDY(0, "待编目"),
    WAIT_RECEIVE(1, "待接收"),
    RECEIVED(2, "已接收"),
    RETURNED(3, "典藏退回"),
    ;

    final Integer status;
    final String statusName;

    BookStateEnum(int status, String statusName) {
        this.status = status;
        this.statusName = statusName;
    }
}
