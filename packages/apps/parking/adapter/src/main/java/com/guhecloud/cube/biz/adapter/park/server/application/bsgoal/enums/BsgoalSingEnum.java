package com.guhecloud.cube.biz.adapter.park.server.application.bsgoal.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BsgoalSingEnum {
    MD5("MD5"),
    SHA1("SHA1");;
    private final String code;
}
