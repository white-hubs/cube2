package com.guhecloud.cube.srv.gen.attendance.management.server.attend.statistics.interfaces.param;

import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.NotNull;

/**
 * 汇总分页列表条件入参
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AttSummaryStatPageParam extends Paging {
    private Long staffId;
    private String staffName;
    private Long deptId;
    private Long groupId;
    @NotNull(message = "时间不能为空")
    private Period period;
    private String periodType;
}
