package com.guhecloud.cube.srv.gen.personnel.management.server.interfaces.vo;

import com.guhecloud.cube.srv.gen.personnel.management.server.enumeration.ArchiveStatus;
import com.guhecloud.cube.srv.gen.personnel.management.server.enumeration.EducationEnum;
import com.guhecloud.cube.srv.gen.personnel.management.server.enumeration.GenderEnum;
import com.guhecloud.cube.srv.gen.personnel.management.server.enumeration.PoliticalStatusEnum;
import com.guhecloud.cube.srv.gen.personnel.management.server.enumeration.ProbationPeriodEnum;
import com.guhecloud.cube.srv.gen.personnel.management.server.enumeration.StaffStatusEnum;
import com.guhecloud.cube.srv.gen.personnel.management.server.enumeration.WorkingStatus;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.Data;
import com.guhecloud.platform.base.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author bq
 * @date 2022/11/08
 * @description 员工花名册
 */
@Data
public class StaffDetailAttendVO {

    private Long id;

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

    private String genderName;

    /**
     * 学历
     */
    private String education;

    private String educationName;

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

    private String politicalStatusName;

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

    private String typeName;

    /**
     * 工号
     */
    private String jobNumber;

    private String positionIds;

    private List<String> postIds;

    private List<Position> positions;

    private String postNames;

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

    private String stateName;

    /**
     * 员工状态
     */
    private String status;

    private String statusName;

    /**
     * 入职日期
     */
    private LocalDateTime inductionDate;

    /**
     * 试用期时长
     */
    private String probationPeriod;

    private String probationPeriodName;

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
     * 资料是否完善
     */
    private boolean perfect;

    private ArchiveStatus archiveStatus;

    @Data
    public static class Position {
        /**
         * 岗位主键
         */
        private Long id;

        /**
         * 岗位名称
         */
        private String name;
    }

    public List<Long> queryPositionsIdList() {
        if (StringUtils.isNotEmpty(this.positionIds)) {
            String[] split = this.positionIds.split(",");
            List<String> list = Arrays.asList(split);
            return list.parallelStream().map(Long::parseLong).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    public boolean getPerfect() {
        return archiveStatus == ArchiveStatus.PERFECTED;
    }

    public void setGender(String gender) {
        this.gender = gender;
        this.genderName = StringUtils.isEmpty(gender) ? "" : GenderEnum.valueOf(gender).getValue();
    }

    public void setEducation(String education) {
        this.education = education;
        this.educationName = StringUtils.isEmpty(education) ? "" : EducationEnum.valueOf(education).getValue();
    }

    public void setPoliticalStatus(String politicalStatus) {
        this.politicalStatus = politicalStatus;
        this.politicalStatusName = StringUtils.isEmpty(politicalStatus) ? "" : PoliticalStatusEnum.valueOf(politicalStatus).getValue();
    }

    public void setStatus(String status) {
        this.status = status;
        this.statusName = StringUtils.isEmpty(status) ? "" : StaffStatusEnum.valueOf(status).getValue();
    }

    public String getStateName() {
        if (StringUtils.isNotEmpty(this.state)) {
            return WorkingStatus.valueOf(this.state).getValue();
        }
        return null;
    }

    public void setProbationPeriod(String probationPeriod) {
        this.probationPeriod = probationPeriod;
        this.probationPeriodName = StringUtils.isEmpty(probationPeriod) ? "" : ProbationPeriodEnum.valueOf(probationPeriod).getValue();
    }

    public String getPostNames() {
        if (!CollectionUtils.isEmpty(this.positions)) {
            return this.positions.parallelStream().map(Position::getName).collect(Collectors.joining(","));
        }
        return "";
    }

    public void setPositionIds(String positionIds) {
        this.positionIds = positionIds;
        if (StringUtils.isNotEmpty(positionIds)) {
            this.postIds = Arrays.asList(positionIds.split(","));
        }
    }
}