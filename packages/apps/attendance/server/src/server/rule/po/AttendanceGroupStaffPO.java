package com.guhecloud.cube.srv.gen.attendance.management.server.rule.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;

/**
 * @author bq
 * @date 2022/11/14
 * @description 考勤组员工
 */
@Data
@TableName("attendance_group_staff")
public class AttendanceGroupStaffPO extends BasicEnhancedTenantEntity<Long> {

    private Long id;

    /**
     * 考勤组ID
     */
    private Long groupId;

    /**
     * 考勤组类型
     */
    private String groupType;

    /**
     * 员工ID（人事花名册）
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

}