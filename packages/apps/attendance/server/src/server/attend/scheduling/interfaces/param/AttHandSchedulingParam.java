package com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.interfaces.param;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

/**
 * 手动排班入参
 */
@Data
public class AttHandSchedulingParam {
    // 月份
    @NotBlank(message = "排班月份不能为空")
    private String month;
    // 人员集合
    @NotNull(message = "排班人员不能为空")
    private List<AttHandStaffParam> hsStaffList;
    // 休息班次是否视为手动拍休息（值为true时,原本没有排班(即休息)也插入休息班次,排班规则修改后不影响
    private Boolean handRest;
}

