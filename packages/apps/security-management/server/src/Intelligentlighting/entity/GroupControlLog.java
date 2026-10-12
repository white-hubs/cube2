package com.guhecloud.cube.srv.gen.securitymanagement.server.Intelligentlighting.entity;

import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GroupControlLog extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    //设备名称
    private String deviceName;
    //组id
    private Long groupControlId;
    //设备id
    private Long deviceId;
    //动作
    private String action;
    //结果
    private String result;
    //操作时间
    private LocalDateTime optTime;
}
