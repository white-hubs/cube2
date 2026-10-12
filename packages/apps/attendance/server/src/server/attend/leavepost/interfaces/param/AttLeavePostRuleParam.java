package com.guhecloud.cube.srv.gen.attendance.management.server.attend.leavepost.interfaces.param;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * 离岗规则
 */
@Data
public class AttLeavePostRuleParam {
    private Long id;
    @NotNull(message = "规则名称不能为空")
    @Length(max = 20, min = 1, message = "名称长度应该在1-20之间")
    private String name;
    /**
     * 离岗前审批
     */
    private Boolean beforeApply;
    /**
     * 最小离岗申请单位（低于该时间不能申请
     */
    @Min(value = 0, message = "最小时间不能为负数")
    private Integer minAskUnit;
    /**
     * 时间计算方式（AttAskLeaveCalcWayEnum（工作日/自然日
     */
    @NotNull(message = "计算方式不能为空")
    private String calculationWay;

}
