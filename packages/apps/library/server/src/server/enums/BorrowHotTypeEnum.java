package com.guhecloud.cube.srv.gen.library.management.server.enums;

import lombok.Getter;

/**
 * 借阅记录热门类型
 */
@Getter
public enum BorrowHotTypeEnum {
    BOOK_AUTHOR("作者"),
    BOOK_NAME("书名"),
    ;
    final String describe;

    BorrowHotTypeEnum(String describe) {
        this.describe = describe;
    }
}
