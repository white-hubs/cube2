package com.guhecloud.cube.srv.gen.securitymanagement.server.common.entity;

import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;

@Data
public class DeviceMonitor extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    //监控信息
    private String monitorName;
    //组态地址
    private String monitorUrl;
    //服务
    private String applicationCode;
}
