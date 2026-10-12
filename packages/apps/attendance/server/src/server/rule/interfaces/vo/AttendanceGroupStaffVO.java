package com.guhecloud.cube.srv.gen.attendance.management.server.rule.interfaces.vo;

import com.mzt.logapi.starter.annotation.DiffLogField;
import lombok.Data;
import com.guhecloud.platform.base.util.StringUtils;

/**
 * @author bq
 * @date 2022/11/14
 * @description 考勤组员工
 */
@Data
public class AttendanceGroupStaffVO {

    private Long id;
    @DiffLogField(name = "考勤组员工")
    private String name;
    private String mobile;

    /**
     * 考勤组ID
     */
    private Long groupId;

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

    /**
     * 部门名称
     */
    private String organizationName;

    public void setName(String name) {
        this.name = name;
        if (StringUtils.isNotEmpty(name)) {
            this.staffName = name;
        }
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
        if (StringUtils.isNotEmpty(mobile)) {
            this.staffMobile = mobile;
        }
    }
}