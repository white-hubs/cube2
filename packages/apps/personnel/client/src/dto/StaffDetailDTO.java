package com.guhecloud.cube.srv.gen.personnel.management.client.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author bq
 * @date 2022/11/08
 * @description 员工花名册
 */
@Data
public class StaffDetailDTO {

    private Long id;

    /**
     * OUAA用户ID。
     */
    private Long userId;

    /**
     * 姓名
     */
    private String name;

    /**
     * 联系电话
     */
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
     * 婚姻状况名称
     */
    private String maritalStatusName;

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
    private Long organizationId;

    /**
     * 部门名称
     */
    private String organizationName;

    /**
     * 职位ID
     */
    private String appointmentId;

    /**
     * 职位名称
     */
    private String appointmentName;

    /**
     * 工作状态
     */
    private String state;

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

}
