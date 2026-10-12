package com.guhecloud.cube.srv.gen.attendance.management.server.rule.commandgateway.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.guhecloud.platform.base.util.StringUtils;

/**
 * @author bq
 * @date 2022/11/15
 * @description 考勤组员工
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubmitAttendanceGroupStaffCommand {

    // 这几个属性和员工的原始属性保持一致，便于查询之后批量复制
    private Long id;
    private String name;
    private String mobile;
    private Long organizationId;

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

    public void setId(Long id) {
        this.id = id;
        if (null != id) {
            this.staffId = id;
        }
    }

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

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
        if (null != organizationId) {
            this.staffOrganizationId = organizationId;
        }
    }

}