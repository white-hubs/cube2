package com.guhecloud.cube.srv.gen.attendance.management.server.clock.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.cube.srv.gen.attendance.management.server.clock.enumeration.ClockStatusEnum;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.annotation.DataPermission;
import com.guhecloud.platform.database.annotation.DataPermissionField;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author appet
 * @date 2022/11/21
 * @description 打卡明细表
 * value = {"SELF:user_id","DEPT:user_dept_id"}
 */
@Data
@DataPermission({
        @DataPermissionField(permission = "SELF", field = "user_id"),
        @DataPermissionField(permission = "DEPT", field = "user_dept_id"),
        @DataPermissionField(permission = "CUSTOM_DEPT", field = "user_dept_id")
}
)
@TableName("classes_clock")
public class ClassesClockPO extends BasicEnhancedTenantEntity<Long> {

    private static final String NO_CARD_NO_CARD = "EXP_NOCARD_NOCARD";

    private Long id;

    private Long userId;

    private String userName;

    private Long userDeptId;//部门ID

    private Long workId;//排班ID

    private Long attendanceGroupId;//考勤组ID

    private Long arrangeClassesId;//打卡班次ID

    private String clockDate;//打卡日期

    private String startWorkTime;//班次开始时间

    private String endWorkTime;//班次结束时间

    private LocalDateTime startRealTime;//班次真正开始时间

    private LocalDateTime endRealTime;//班次真正结束时间

    private LocalDateTime startWorkClockTime;//上班打卡时间

    private LocalDateTime endWorkClockTime;//下班打卡时间

    private String startWorkClockStatus;//上班打卡状态

    private String endWorkClockStatus;//下班打卡状态

    private String classesClockStatus;//班次打卡状态

    private Boolean startLeave;//上班卡是否请假

    private Boolean endLeave;//下班卡是否请假

    private String startTrigger;//上班卡审批动作

    private String endTrigger;//下班卡审批动作

    public void parseClassesClockStatus() {
        this.startWorkClockStatus =
                StringUtils.isNotEmpty(this.startWorkClockStatus) ? this.startWorkClockStatus : ClockStatusEnum.EXP_NOCARD.name();
        if (StringUtils.isNotEmpty(this.endWorkClockStatus) && !ClockStatusEnum.NORMAL.name().equals(this.endWorkClockStatus)) {
            if (ClockStatusEnum.NORMAL.name().equals(this.startWorkClockStatus)) {
                this.classesClockStatus = this.endWorkClockStatus;
            } else {
                String clockStatus = this.startWorkClockStatus + this.endWorkClockStatus.substring(this.endWorkClockStatus.indexOf("_"));
                this.classesClockStatus = NO_CARD_NO_CARD.equals(clockStatus) ? ClockStatusEnum.EXP_SKIP_WORK.name() : clockStatus;
            }
        } else {
            this.classesClockStatus = this.startWorkClockStatus;
        }
    }

}