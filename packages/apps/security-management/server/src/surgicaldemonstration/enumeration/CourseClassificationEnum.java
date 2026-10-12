package com.guhecloud.cube.srv.gen.securitymanagement.server.surgicaldemonstration.enumeration;

public enum CourseClassificationEnum {
    DISTANCE_TRAINING("远程培训"),
    REMOTE_GUIDANCE("远程指导"),
    DEPARTMENTAL_TRAINING("科室内训"),
    REMOTE_CONSULTATION("远程会诊");

    /**
     * 描述信息
     */
    private final String description;

    CourseClassificationEnum(String description) {
        this.description = description;
    }
}
