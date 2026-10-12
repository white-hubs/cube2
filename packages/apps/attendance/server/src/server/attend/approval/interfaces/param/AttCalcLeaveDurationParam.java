package com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.interfaces.param;

import com.guhecloud.platform.base.model.Period;
import lombok.Data;

import jakarta.validation.constraints.NotNull;

/**
 * 计算请假时长入参
 */
@Data
public class AttCalcLeaveDurationParam {
    @NotNull(message = "业务类型不能为空")
    private String businessType;
    @NotNull(message = "人员不能为空")
    private Long staffId;
    @NotNull(message = "时间不能为空")
    private Period period;
    /**
     * 假期类型
     */
    private Long holidayRuleId;
}
