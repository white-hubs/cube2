package com.guhecloud.cube.srv.gen.securitymanagement.server.environmental.enumeration;

public enum ParagraphEnum {
    INLET_SECTION("入口段"),
    BASIC_SEGMENT("基本段"),
    EXIT_SECTION("出口段");
    /**
     * 描述信息
     */
    private final String description;

    ParagraphEnum(String description) {
        this.description = description;
    }
}
