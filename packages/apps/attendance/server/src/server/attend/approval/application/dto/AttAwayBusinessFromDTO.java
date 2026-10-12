package com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.application.dto;

import com.guhecloud.cube.srv.gen.attendance.management.server.overtime.annotation.FormField;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 出差单
 */
@Data
public class AttAwayBusinessFromDTO {
    private Long id;
    @FormField(label = "用户ID")
    private Long applicantId;
    private String applicantName;
    @FormField(label = "部门ID")
    private Long deptId;
    private String deptName;
    @FormField(label = "职位id")
    private String postIds;
    private String postName;
    private Long awayRuleId;
    private String awayRuleName;
    @FormField(label = "电话")
    private String phone;
    @FormField(label = "出差开始时间")
    private LocalDateTime awayStartTime;
    @FormField(label = "出差结束时间")
    private LocalDateTime awayEndTime;
    // 出差时长
    @FormField(label = "出差时长")
    private Float awayNumber;
    @FormField(label = "出差地点")
    private String awayLocation;
    // 出差备注
    private String awayRemark;
    // 文件的主键
    private Long fileKey;
    // 出差单(审批)状态
    private String status;
    // 流程实例id
    private String processInstanceId;

}
