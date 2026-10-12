package com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.param;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GearPositionTimeParam {
    //档位
    private Long gearPositionId;
    //档位名称
    private String gearPositionName;
    //档位号
    private String gearPositionCode;
    //计费时间
    private LocalDateTime billableTime;
    //档位区域
    private String gearPositionArea;
    //区域id
    private String gearPositionAreaId;
    //部门
    private String department;
    //部门id
    private String departmentId;
}
