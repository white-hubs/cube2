package com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.interfaces.result;

import lombok.Data;

@Data
public class AttGroupLeavePostRuleResult {
    private Long id;
    private String name;
    /**
     * 离岗前发起
     */
    private Boolean beforeApply;
    /**
     * 最小离岗申请单位（低于该时间不能申请
     */
    private Integer minAskUnit;
    /**
     * 时间计算方式（AttDurationCalcWayEnum（工作日/自然日
     */
    private String calculationWay;
}
