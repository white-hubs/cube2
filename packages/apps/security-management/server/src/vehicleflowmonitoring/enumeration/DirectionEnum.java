package com.guhecloud.cube.srv.gen.securitymanagement.server.vehicleflowmonitoring.enumeration;

import lombok.Getter;

@Getter
public enum DirectionEnum {

    DIRECTION_IN("驶入"),
    DIRECTION_OUT("驶出"),
    ;

    private final String description;

    DirectionEnum(String description) {
        this.description = description;
    }
}
