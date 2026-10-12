package com.guhecloud.cube.srv.gen.securitymanagement.server.common.vo;

import lombok.Data;

@Data
public class DeviceMonitorVo {
    private Long id;
    //监控信息
    private String monitorName;
    //组态地址
    private String monitorUrl;
    //服务
    private String applicationCode;
}
