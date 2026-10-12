package com.guhecloud.cube.srv.gen.maintain.server.upkeep.enumeration;

import lombok.NonNull;

/**
 * @Author: lijie
 * @CreateTime: 2024-04-25  18:06
 * @Description:
 * @Version: 1.0
 */
public enum MaintenaceDetailsEnum {
    EQ("等于"),
    GT("大于");

    /**
     * 描述信息
     */
    private final String description;

    MaintenaceDetailsEnum(@NonNull String description) {
        this.description = description;
    }
}
