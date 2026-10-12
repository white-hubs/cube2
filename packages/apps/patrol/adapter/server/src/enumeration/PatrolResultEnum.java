package com.guhecloud.cube.srv.gen.adapter.patrol.server.enumeration;

public enum PatrolResultEnum {
    NORMAL("正常"),
    NORMAL_EARLY("早到"),
    NORMAL_LATE("迟到"),
    EXP_MISS("漏检");

    String desc;

    PatrolResultEnum(String desc) {
        this.desc = desc;
    }
}
