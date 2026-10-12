package com.guhecloud.cube.srv.gen.adapter.access.control.server.enumeration;

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
