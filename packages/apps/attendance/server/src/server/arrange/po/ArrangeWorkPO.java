package com.guhecloud.cube.srv.gen.attendance.management.server.arrange.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.cube.srv.gen.attendance.management.server.filter.AttendanceCurrentUserDeptFieldFilter;
import com.guhecloud.platform.database.annotation.DataPermission;
import com.guhecloud.platform.database.annotation.DataPermissionField;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import com.mzt.logapi.starter.annotation.DiffLogField;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author bq
 * @date 2022/11/16
 * @description 排班管理
 */
@Data
@TableName("arrange_work")
@DataPermission({
        @DataPermissionField(permission = "CURRENT_USER", field = "staff_id"),
        @DataPermissionField(permission = "CURRENT_USER_DEPT", field = "staff_organization_id",
                             fieldFilter = AttendanceCurrentUserDeptFieldFilter.class
        ),
        @DataPermissionField(permission = "CUSTOM_DEPT", field = "staff_organization_id")
}
)
public class ArrangeWorkPO extends BasicEnhancedTenantEntity<Long> {

    private Long id;

    /**
     * 员工ID
     */
    private Long staffId;

    /**
     * 员工姓名
     */
    private String staffName;

    /**
     * 员工电话
     */
    private String staffMobile;

    /**
     * 员工部门
     */
    private Long staffOrganizationId;

    /**
     * 班次IDS
     */
    @DiffLogField(name = "班次", function = "ARRANGE_WORK_CLASSES")
    private String arrangeWorkClassesIds;

    /**
     * 考勤组ID
     */
    private Long groupId;

    /**
     * 月份
     */
    private String month;

    /**
     * 周几
     */
    private Integer week;

    /**
     * 日期
     */
    private LocalDateTime date;

    /**
     * 是否休息
     */
    private Boolean isRest;

    /**
     * 是否节日
     */
    private Boolean isHoliday;

}