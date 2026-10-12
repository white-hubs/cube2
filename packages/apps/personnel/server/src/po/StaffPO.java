package com.guhecloud.cube.srv.gen.personnel.management.server.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.cube.srv.gen.personnel.management.server.enumeration.ArchiveStatus;
import com.guhecloud.cube.srv.gen.personnel.management.server.filter.StaffDeptFieldFilter;
import com.guhecloud.cube.srv.gen.personnel.management.server.filter.StaffDeptFieldParamConvert;
import com.guhecloud.platform.database.annotation.DataPermission;
import com.guhecloud.platform.database.annotation.DataPermissionField;
import com.guhecloud.platform.database.annotation.Unique;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author bq
 * @date 2022/11/08
 * @description 员工花名册
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@TableName("staff")
@EqualsAndHashCode(callSuper = false)
@DataPermission({
        @DataPermissionField(permission = "CURRENT_USER", field = "user_id"),
        @DataPermissionField(permission = "CURRENT_USER_DEPT", field = "user_id", fieldFilter = StaffDeptFieldFilter.class),
        @DataPermissionField(permission = "CUSTOM_DEPT", field = "user_id", fieldParamConvert = StaffDeptFieldParamConvert.class)
}
)
public class StaffPO extends BasicEnhancedTenantEntity<Long> {

    private Long id;

    private Long userId;

    /**
     * 档案状态
     */
    private ArchiveStatus archiveStatus;

    /**
     * 姓名
     */
    private String name;

    /**
     * 联系电话
     */
    @Unique(message = "联系电话唯一")
    private String mobile;

    /**
     * 性别
     */
    private String gender;

    /**
     * 婚姻状况
     */
    private String maritalStatus;

    /**
     * 出生日期
     */
    private LocalDateTime dateOfBirth;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 学历
     */
    private String education;

    /**
     * 专业
     */
    private String speciality;

    /**
     * 毕业院校
     */
    private String university;

    /**
     * 民族
     */
    private String nation;

    /**
     * 政治面貌
     */
    private String politicalStatus;

    /**
     * 地址
     */
    private String address;

    /**
     * 紧急联系人
     */
    private String emergencyContact;

    /**
     * 紧急联系人电话
     */
    private String emergencyContactMobile;

    /**
     * 员工类型
     */
    private String type;

    /**
     * 工号
     */
    private String jobNumber = "";

    /**
     * 岗位ID
     */
    private String positionIds;

    /**
     * 部门ID
     */
    private Long organizationId;

    /**
     * 职位ID
     */
    private String appointmentId;

    /**
     * 工作状态
     */
    private String state = "";

    /**
     * 员工状态
     */
    private String status;

    /**
     * 入职日期
     */
    private LocalDateTime inductionDate;

    /**
     * 司龄
     */
    private Integer companyYears;

    /**
     * 试用期时长
     */
    private String probationPeriod;

    /**
     * 计划转正日期
     */
    private LocalDateTime planRegularWorkerDate;

    /**
     * 转正日期
     */
    private LocalDateTime regularWorkerDate;

    /**
     * 离职日期
     */
    private LocalDateTime quitDate;

    /**
     * 首次社保缴纳日期
     */
    private LocalDateTime paySocialInsuranceFirstDate;

    /**
     * 工龄
     */
    private Integer workingYears;

    /**
     * 所在区域
     */
    private String areaIds;

    /**
     * 身份证号
     */
    private String idCard;
}
