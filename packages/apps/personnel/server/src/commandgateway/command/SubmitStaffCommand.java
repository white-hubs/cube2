package com.guhecloud.cube.srv.gen.personnel.management.server.commandgateway.command;

import com.guhecloud.platform.base.util.CollectionUtils;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author bq
 * @date 2022/10/18
 * @description 提交员工信息命令
 */
@Data
public class SubmitStaffCommand {

    private Long id;

    /**
     * 姓名
     */
    @Length(max = 20, message = "姓名限制在20字内")
    @NotEmpty(message = "姓名不能为空")
    private String name;

    /**
     * 联系电话
     */
    @NotEmpty(message = "联系电话不能为空")
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
    private String jobNumber;

    /**
     * 部门ID
     */
    @NotNull(message = "部门不能为空")
    private Long organizationId;

    /**
     * 岗位IDS
     */
    private List<Long> postIds;

    private List<Long> roleIds;

    private String positionIds;

    /**
     * 职位ID
     */
    private List<Long> appointmentIds;

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
     * 多个区域id集合
     */
    private List<Long> areaIdList;

    private String areaIds;

    /**
     * 身份证号
     */
    private String idCard;

    /**
     * 短号
     */
    private String cornet;

    public void setPostIds(List<Long> postIds) {
        this.postIds = postIds;
        if (!CollectionUtils.isEmpty(postIds)) {
            this.positionIds = postIds.stream().map(String::valueOf).collect(Collectors.joining(","));
        }
    }
}
