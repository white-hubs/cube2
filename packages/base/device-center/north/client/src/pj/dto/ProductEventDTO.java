package com.guhecloud.cube.iot.north.bridge.client.pj.dto;

import lombok.Data;

@Data
public class ProductEventDTO {
    private String eventName;
    private String eventCode;
    private EventConfig eventConfig;
    private String eventConfigStr;
    private String categoryCode;
    private String categoryName;
    private String remark;
}
