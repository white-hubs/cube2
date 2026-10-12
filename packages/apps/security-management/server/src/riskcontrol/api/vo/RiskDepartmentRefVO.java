package com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.vo;

import lombok.Data;

@Data
public class RiskDepartmentRefVO {
    /**
     * 部门 ID。
     */
    private String departmentId;

    /**
     * 部门名称，由 OUAA 组织服务解析得到。
     */
    private String departmentName;
}
