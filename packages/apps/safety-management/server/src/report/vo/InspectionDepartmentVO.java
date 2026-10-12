package com.guhecloud.cube.srv.gen.safety.management.report.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InspectionDepartmentVO {
    /**
     * 排名
     */
    private Integer index;
    /**
     * 部门名称
     */
    private String organizationName;
    /**
     * 巡检次数
     */
    private Long num;
    /**
     * 异常次数
     */
    private Long abnormalNum;
    /**
     * 异常率
     */
    private String abnormalRate;

    /**
     * 巡更频率
     */
    private String patrolRate;
}
