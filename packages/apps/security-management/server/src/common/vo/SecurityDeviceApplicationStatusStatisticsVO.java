package com.guhecloud.cube.srv.gen.securitymanagement.server.common.vo;

import lombok.Data;

/**
 * 安全设备按应用分组的在线、离线数量统计。
 */
@Data
public class SecurityDeviceApplicationStatusStatisticsVO {

    private String applicationCode;

    private String applicationName;

    private Integer onlineNum;

    private Integer offlineNum;
}
