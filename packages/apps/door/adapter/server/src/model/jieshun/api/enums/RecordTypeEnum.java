package com.guhecloud.cube.srv.gen.adapter.access.control.server.model.jieshun.api.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum RecordTypeEnum {
    UNKNOWN(0, "未知"),
    NORMAL(1, "正常记录"),
    ILLEGAL(2, "非法记录"),
    ALARM(3, "报警记录");
    private final Integer code;
    private final String desc;

    public static String getDescBy(Integer recordType) {
        for (RecordTypeEnum recordTypeEnum : RecordTypeEnum.values()) {
            if (recordTypeEnum.getCode().equals(recordType)) {
                return recordTypeEnum.getDesc();
            }
        }
        throw new RuntimeException("未知的记录类型");

    }
}
