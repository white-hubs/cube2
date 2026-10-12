package com.guhecloud.cube.srv.gen.disinfection.server.disinfection.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum DisinfectionPlanStatusEnum {

    EFFECTIVE("生效中"),
    DISABLED("已停用"),
    ENDED("已结束"),
    NOT_STARTED("未开始");
    private final String value;

}
