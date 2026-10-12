package com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.application.dto;

import com.guhecloud.cube.srv.gen.attendance.management.server.attend.common.AttConstants;
import com.guhecloud.cube.srv.gen.attendance.management.server.overtime.annotation.FormField;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 离岗单
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttLeavePostFromDTO {
    private Long id;
    @FormField(label = "员工id")
    private Long applicantId;
    private String applicantName;
    @FormField(label = "部门id")
    private Long deptId;
    private String deptName;
    @FormField(label = "岗位id")
    private String postIds;
    private String postName;
    private Long postRuleId;
    private String postRuleName;
    @FormField(label = "电话")
    private String phone;
    @FormField(label = "离岗开始时间")
    private LocalDateTime postStartTime;
    @FormField(label = "离岗结束时间")
    private LocalDateTime postEndTime;
    // 离岗时长
    @FormField(label = "离岗时长")
    private Float postNumber;
    @FormField(label = "离岗地点")
    private String postLocation;
    // 离岗备注
    private String postRemark;
    // 文件的主键
    private Long fileKey;
    // 离岗单(审批)状态
    private String status;
    // 扣除的记录id
    private String deadlineIds;
    // 流程实例id
    private String processInstanceId;

    public void businessCheck() {
        if (applicantId == null) {
            throw new ServiceException("申请人不能为空！");
        }
        if (postStartTime == null || postEndTime == null) {
            throw new ServiceException("离岗时间不能为空！");
        }
        if (postNumber == null || postNumber <= AttConstants.zeroInt) {
            throw new ServiceException("离岗时时长应该大于0！");
        }
    }
}
