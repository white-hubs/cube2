package com.guhecloud.cube.biz.door.server.enumeration;

import lombok.Getter;

@Getter
public enum EventResultEnum {
    DO_REFUSE("拒绝"),
    DO_PASS("通过");

    private final String desc;

    EventResultEnum(String desc) {
        this.desc = desc;
    }
}
