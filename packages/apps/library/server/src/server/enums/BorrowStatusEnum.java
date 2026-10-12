package com.guhecloud.cube.srv.gen.library.management.server.enums;

import lombok.Getter;

/**
 * 借阅状态
 */
@Getter
public enum BorrowStatusEnum {
    RETURNED(1, "已归还"),
    NOT_RETURNED(2, "未归还"),
    OVERDUE(3, "已预期"),
    ;

    final Integer status;
    final String statusName;

    BorrowStatusEnum(int status, String statusName) {
        this.status = status;
        this.statusName = statusName;
    }
}
