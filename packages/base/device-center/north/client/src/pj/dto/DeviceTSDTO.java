package com.guhecloud.cube.iot.north.bridge.client.pj.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 设备历史属性
 */
@Setter
@Getter
public class DeviceTSDTO {

    private String deviceId;

    private String deviceName;

    private String deviceNumber;

    private List<PropertyTsDTO> propertyTsBoList;
}
