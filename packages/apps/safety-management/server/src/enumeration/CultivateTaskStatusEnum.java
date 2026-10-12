package com.guhecloud.cube.srv.gen.safety.management.enumeration;

import lombok.Getter;

@Getter
public enum CultivateTaskStatusEnum {
    WAIT_PUBLISH("待发布"),
    OVER("已结束");

    private final String description;

    CultivateTaskStatusEnum(String description) {
        this.description = description;
    }
}
