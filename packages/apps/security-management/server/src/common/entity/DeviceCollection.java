package com.guhecloud.cube.srv.gen.securitymanagement.server.common.entity;

import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;

@Data
public class DeviceCollection extends BasicEnhancedTenantEntity<Long> {
    private Long id;

    //应用
    private String applicationCode;

    //设备id
    private Long deviceId;

    //用户
    private Long userId;
}
