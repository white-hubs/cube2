package com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.interfaces.param;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AttAwayBusinessDurationParam {
    @NotNull(message = "人员不能为空")
    private Long staffId;
    @NotNull(message = "申请开始时间不能为空")
    private LocalDateTime startTime;
    @NotNull(message = "申请结束时间不能为空")
    private LocalDateTime endTime;

}
