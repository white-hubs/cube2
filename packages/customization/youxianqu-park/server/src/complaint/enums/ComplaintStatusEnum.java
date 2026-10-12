package com.guhecloud.cube.customized.youxianqu.park.management.server.complaint.enums;

import lombok.Getter;

/**
 * 投诉处理状态枚举
 */
@Getter
public enum ComplaintStatusEnum {
    NOT_PROCESSED(1, "未处理"),
    DURING_PROCESSING(2, "处理中"),
    PROCESSING_COMPLETED(3, "处理完成"),
    ;

    final Integer code;

    final String describe;

    ComplaintStatusEnum(Integer code, String describe) {
        this.code = code;
        this.describe = describe;
    }
}
