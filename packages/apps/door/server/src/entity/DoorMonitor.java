package com.guhecloud.cube.biz.door.server.entity;

import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;

@Data
public class DoorMonitor extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    //控制信息
    private String monitorName;
    //控制组态链接
    private String monitorUrl;
}
