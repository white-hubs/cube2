package com.guhecloud.cube.biz.adapter.park.server.application.iot.facade.response;

import lombok.Data;

import java.util.List;

/**
 * @author mioopp
 * @create 2021-11-20 9:48
 */
@Data
public class IotDeviceResponse {

    private String deviceId;

    private String deviceName;

    private String prodId;

    private String prodCode;

    private String prodName;

    private List<String> capabilities;

    private List<PropertySpec> properties;

    private List<KeyValueWithTs> snapshots;

    private String rawDeviceId;

    private String locationCode;

    private String tags;

    private String status;

    private String createTs;

    private String createBy;

}
