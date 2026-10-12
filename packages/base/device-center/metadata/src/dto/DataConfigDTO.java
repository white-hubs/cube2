package com.guhecloud.cube.base.device.center.metadata.dto;

import lombok.Data;

@Data
public class DataConfigDTO {
    private String dataType;
    private String format;
    private ConstraintsDTO dataConfig;
}
