package com.guhecloud.cube.srv.gen.adapter.access.control.server.model.jieshun.api.dto.request;

import lombok.Data;

/**
 * author: ht
 * create: 2025-05-22 16:40
 */
@Data
public class Device {
    /**
     * {
     * "deviceGuid": "69c22997-3b48-44e1-867c-1190bac3b911",
     * "deviceId": "1684982784",
     * "deviceName": "速通 II 停车场 124",
     * "deviceIp": "172.16.20.124",
     * "deviceGateway": "172.16.20.1",
     * "deviceNetmask": "255.255.252.0",
     * "deviceIoType": 1,
     * "parentId": "284086272",
     * "remark": ""
     * }
     */
    private String deviceGuid;
    private String deviceId;
    private String deviceName;
    private String deviceIp;
    private String deviceGateway;
    private String deviceNetmask;
    private Integer deviceIoType;
    private String parentId;
    private String remark;



}
