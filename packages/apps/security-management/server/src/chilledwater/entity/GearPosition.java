package com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.entity;

import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GearPosition extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    //批发商
    private Long wholesaler;
    //档位
    private Long gearPositionId;
    //档位
    private String gearPositionName;
    //区域
    private String gearPositionArea;
    //区域id
    private String gearPositionAreaId;
    //部门
    private String department;
    //部门id
    private String departmentId;
    //计费时间
    private LocalDateTime billableTime;
    //档位编号
    private String gearPositionCode;

}
