package com.guhecloud.cube.iot.north.bridge.client.pj.dto;

import lombok.Data;

import java.util.List;

@Data
public class DeviceDetailDTO {
    private String deviceId;
    private String deviceParentId;
    private String deviceNumber;
    private String productId;
    private String productName;
    private String deviceName;
    private Integer onlineState;
    private String tags;

    private List<ProductPropDTO> productPropVoList;
    private List<ProductEventDTO> productEventVoList;
    private List<Object> productCommandVoList;
    private List<KeyValueWithTsDTO> snapshots;
}
