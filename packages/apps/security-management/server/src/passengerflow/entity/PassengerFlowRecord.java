package com.guhecloud.cube.srv.gen.securitymanagement.server.passengerflow.entity;

import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PassengerFlowRecord extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    private String recordId;
    private String areaName;
    private String recordDevice;
    private LocalDateTime recordTime;
    private String inOut;
    private int recordNum;
}
