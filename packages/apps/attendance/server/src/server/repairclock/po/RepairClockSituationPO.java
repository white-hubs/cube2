package com.guhecloud.cube.srv.gen.attendance.management.server.repairclock.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.cube.srv.gen.attendance.management.server.overtime.annotation.FormField;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author appet
 * @date 2022/11/21
 * @description 补卡明细表
 */
@Data
@TableName("repair_clock_situation")
public class RepairClockSituationPO extends BasicEnhancedTenantEntity<Long> {

    private Long id;
    @FormField(label = "用户ID")
    private Long applicantId;//申请人ID

    private String applicantName;//申请人姓名
    @FormField(label = "部门ID")
    private Long applicantDeptId;//部门ID

    private Long attendanceGroupId;//考情组ID

    private LocalDateTime applyTime;//申请时间

    private LocalDateTime applyDoneTime;//申请处理完成时间
    @FormField(label = "班次ID")
    private Long classesId;//班次ID
    @FormField(label = "补签日期")
    private String date;//补卡日期
    private String clockType;//打卡类型 START END
    private String expType;//异常类型 EXP_LATE("迟到"),EXP_LEAVE_EARLY("早退"),EXP_NOCARD("缺卡"),
    private LocalDateTime clockTime;//补卡时间
    @FormField(label = "补卡理由")
    private String reason;//补卡理由
    @FormField(label = "备注")
    private String remark;
}