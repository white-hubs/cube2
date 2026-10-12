package com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.interfaces.param;

import lombok.Data;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.time.LocalDateTime;

/**
 * 离岗时长计算入参
 */
@Data
public class AttLeavePostDurationParam {
    @NotNull(message = "人员不能为空")
    private Long staffId;
    @NotNull(message = "离岗开始时间不能为空")
    private LocalDateTime postStartTime;
    @NotNull(message = "离岗结束时间不能为空")
    private LocalDateTime postEndTime;
}
