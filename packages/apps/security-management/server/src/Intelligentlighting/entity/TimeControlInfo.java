package com.guhecloud.cube.srv.gen.securitymanagement.server.Intelligentlighting.entity;

import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TimeControlInfo extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    //计划名称
    private String planName;
    //时间周期
    private String timeCycle;
    //开始时间
    private LocalDateTime planStartTime;
    //计划结束时时间
    private LocalDateTime planEndTime;
    //是否启用
    private Boolean enabled;
    //回路数
    private Long num;
}
