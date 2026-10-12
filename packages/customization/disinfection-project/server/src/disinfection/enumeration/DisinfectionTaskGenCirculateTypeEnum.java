package com.guhecloud.cube.srv.gen.disinfection.server.disinfection.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum DisinfectionTaskGenCirculateTypeEnum {

    MONTHLY("每月"),
    WEEKLY("每周"),
    DAILY("每天");
    private final String desc;
}
