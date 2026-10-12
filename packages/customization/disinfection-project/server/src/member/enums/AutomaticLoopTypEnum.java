package com.guhecloud.cube.srv.gen.disinfection.server.member.enums;

import lombok.Getter;

@Getter
public enum AutomaticLoopTypEnum {
    // 天
    DAY("每天"),
    // 周
    WEEK("每周"),
    // 月
    MONTH("每月"),
    ;
    final String displayName;

    AutomaticLoopTypEnum(String displayName) {
        this.displayName = displayName;
    }
}
