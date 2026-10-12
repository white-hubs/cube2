package com.guhecloud.cube.srv.gen.attendance.management.server.repairclock.commandgateway.command;

import com.guhecloud.platform.base.model.Period;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author appet
 * @date 2022/11/29
 * @description 补卡申请
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClockRepairCommand {
    //申请人ID
    private Long applicantId;
    //申请人姓名
    private String applicantName;
    //申请人部门
    private Long applicantDeptId;
    //补卡日期
    private String date;
    //补卡时间
    private LocalDateTime clockTime;
    //班次ID
    private String classesId;
    //打卡类型
    private String clockType;
    //异常类型
    private String expType;
    //加班原因
    private String reason;
    //备注
    private String remark;
    //考情组ID
    private Long attendanceGroupId;
    //申请时间
    private LocalDateTime applyTime;
    //审批完成时间
    private LocalDateTime applyDoneTime;
    private String workDuration;
    //加班时间
    private Period workPeriod;

}