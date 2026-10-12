package com.guhecloud.cube.srv.gen.maintain.server.upkeep.enumeration;

import lombok.Getter;
import lombok.NonNull;

@Getter
public enum MaintenaceTaskStatusEnum {
    BE_MAINTAINED("待维保"),
    COMPLETED("已完成");

    private final String description;

    MaintenaceTaskStatusEnum(@NonNull String description) {
        this.description = description;
    }

}
