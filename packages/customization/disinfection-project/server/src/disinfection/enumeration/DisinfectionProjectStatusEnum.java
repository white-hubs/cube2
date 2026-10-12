package com.guhecloud.cube.srv.gen.disinfection.server.disinfection.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum DisinfectionProjectStatusEnum {

    WAITING("待开始"),
    PROCESSING("进行中"),
    COMPLETED("已完成"),
    ;
    private final String desc;
}
