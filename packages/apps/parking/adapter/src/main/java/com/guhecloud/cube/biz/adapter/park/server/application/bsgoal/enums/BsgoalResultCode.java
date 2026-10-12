package com.guhecloud.cube.biz.adapter.park.server.application.bsgoal.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BsgoalResultCode {
    ERROR(-1, "异常"),
    SUCCESS(0, "成功"),
    NO_RECORD(1, "无在场记录"),
    CONVERT_INPUT(2, "该扫码设备未入场转入输车牌出场"),
    PROHIBIT_OUT(3, "扫码输车牌匹配不到入场记录禁止出场"),
    ;

    private final int code;
    private final String desc;
}
