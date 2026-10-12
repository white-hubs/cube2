package com.guhecloud.cube.srv.gen.attendance.management.server.overtime.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.cube.srv.gen.attendance.management.server.overtime.annotation.FormField;
import com.guhecloud.platform.database.annotation.DataPermission;
import com.guhecloud.platform.database.annotation.DataPermissionField;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author appet
 * @date 2022/11/21
 * @description 加班明细表
 */
@Data
@TableName("work_overtime_situation")
@DataPermission({
        @DataPermissionField(permission = "SELF", field = "applicant_id"),
        @DataPermissionField(permission = "DEPT", field = "applicant_dept_id"),
        @DataPermissionField(permission = "CUSTOM_DEPT", field = "applicant_dept_id")
}
)
public class WorkOvertimeSituationPO extends BasicEnhancedTenantEntity<Long> {

    private Long id;
    @FormField(label = "用户ID")
    private Long applicantId;//申请人ID
    //    @FormField(label = "姓名")
    private String applicantName;//申请人姓名
    @FormField(label = "部门ID")
    private Long applicantDeptId;//部门ID

    private Long attendanceGroupId;//考情组ID

    private LocalDateTime applyTime;//申请时间

    private LocalDateTime applyDoneTime;//申请处理完成时间
    @FormField(label = "加班类型")
    private String workType;//加班类型
    //    @FormField(label = "开始时间")
    private LocalDateTime startTime;//加班开始时间
    //    @FormField(label = "结束时间")
    private LocalDateTime endTime;//加班结束时间

    private String workDate;//加班日期
    @FormField(label = "加班时段")
    private String workPeriod;//加班时段
    @FormField(label = "加班时长")
    private Float workDuration;//加班时长
    @FormField(label = "加班补偿")
    private String compensate;//补偿方案

    @FormField(label = "加班原因")
    private String reason;

    private Long workRuleId;//这张表（attendance_group_work_overtime_rule）的ID
    @FormField(label = "批量加班信息")
    private List overtimeDetails;

}