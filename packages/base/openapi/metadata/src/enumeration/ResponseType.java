package com.guhecloud.cube.base.openapi.metadata.enumeration;

public enum ResponseType {
    TEXT("text"),
    UNKNOWN("unknown"),
    ;
    private final String type;

    ResponseType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
