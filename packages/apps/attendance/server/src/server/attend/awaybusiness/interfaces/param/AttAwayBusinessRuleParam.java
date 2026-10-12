package com.guhecloud.cube.srv.gen.attendance.management.server.attend.awaybusiness.interfaces.param;

import lombok.Data;

@Data
public class AttAwayBusinessRuleParam {
    private Long id;
    private String name;
    /**
     * 提前前发起
     */
    private Boolean beforeApply;
    /**
     * 最小申请单位（低于该时间不能申请
     */
    private Integer minAskUnit;
    /**
     * 时间计算方式（AttDurationCalcWayEnum（工作日/自然日
     */
    private String calculationWay;
}
