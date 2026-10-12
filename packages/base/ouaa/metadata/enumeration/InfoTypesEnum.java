package com.guhecloud.cube.biz.ouaa.metadata.enumeration;

public enum InfoTypesEnum {

    ORGANIZATION("部门");

    private final String name;

    InfoTypesEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
