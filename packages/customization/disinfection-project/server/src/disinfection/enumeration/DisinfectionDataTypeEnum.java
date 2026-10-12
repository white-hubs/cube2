package com.guhecloud.cube.srv.gen.disinfection.server.disinfection.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum DisinfectionDataTypeEnum {

    PROJECT("project"),
    TASK("task"),
    FILE("file"),
    PLACE("place"),
    ;
    private final String dataType;

}
