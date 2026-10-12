package com.guhecloud.cube.srv.gen.disinfection.server.disinfection.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DisinfectionTaskTypeEnum {

    RECTIFY_WORK("整改作业"),
    EMERGENCY_DISINFECTION("应急消杀"),
    DAILY_DISINFECTION("日常消杀");;
    private final String code;
}
