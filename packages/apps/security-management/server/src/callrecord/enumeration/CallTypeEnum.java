package com.guhecloud.cube.srv.gen.securitymanagement.server.callrecord.enumeration;

public enum CallTypeEnum {
    EMERGENT("紧急"),
    ORDINARY("普通"),
    STRENGTHEN("加强");
    /**
     * 描述信息
     */
    private final String description;

    CallTypeEnum(String description) {
        this.description = description;
    }

}
