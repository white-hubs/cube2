package com.guhecloud.cube.srv.gen.attendance.management.server.attend.overtime.param.query;

import com.guhecloud.platform.base.model.Period;
import lombok.Data;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Data
public class WorkOvertimePeriodQueryParam {
    //加班类型
    @NotEmpty
    private String workOvertimeType;
    //加班开始时间、结束时间
    @NotNull
    private Period period;

}
