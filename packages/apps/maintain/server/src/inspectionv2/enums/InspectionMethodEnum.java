package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.enums;

import lombok.Getter;

/**
 * @Author: lijie
 * @CreateTime: 2023-11-14  09:20
 * @Description:
 * @Version: 1.0
 */
@Getter
public enum InspectionMethodEnum {
    SCAN_CODE("扫码巡检");
    private String description;

    InspectionMethodEnum(String description) {
        this.description = description;
    }
}
