package com.guhecloud.cube.srv.gen.securitymanagement.server.electrical.enumeration;

import lombok.NonNull;

public enum StatisticsType {

    DISTRIBUTION("配电"),
    ELECTRICITY("用电");

    public final String desc;

    StatisticsType(@NonNull String desc) {
        this.desc = desc;
    }
}
