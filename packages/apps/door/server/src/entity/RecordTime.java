package com.guhecloud.cube.biz.door.server.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RecordTime extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;
}
