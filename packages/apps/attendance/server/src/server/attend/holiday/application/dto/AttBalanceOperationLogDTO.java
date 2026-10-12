package com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.application.dto;

import com.mzt.logapi.starter.annotation.DiffLogField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttBalanceOperationLogDTO {
    @DiffLogField(name = "id")
    private Long id;
    @DiffLogField(name = "人员")
    private String staffName;
    @DiffLogField(name = "假期")
    private String holidayName;
    @DiffLogField(name = "余额")
    private String balance;
    @DiffLogField(name = "单位")
    private String unit;
    @DiffLogField(name = "有效期")
    private String deadline;
    @DiffLogField(name = "备注")
    private String remark;
}
