package com.guhecloud.cube.srv.gen.reportmanagement.server.enumeration;

/**
 *
 */

public enum WorkPlanStatusEnum {
    WAIT("待发布"),
    OVER("已发布");
    private final String description;

    WorkPlanStatusEnum(String description) {
        this.description = description;
    }
}
