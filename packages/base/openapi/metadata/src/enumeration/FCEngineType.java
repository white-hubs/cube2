package com.guhecloud.cube.base.openapi.metadata.enumeration;

public enum FCEngineType {
    JAVA("java"),
    JS("javascript"),
    ;

    private final String type;

    FCEngineType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
