package com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.application.dto;

import com.guhecloud.cube.srv.gen.attendance.management.server.attend.common.AttConstants;
import com.guhecloud.cube.srv.gen.attendance.management.server.overtime.annotation.FormField;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.DateUtils;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 请假单dto
 */
@Data
public class AttAskLeaveFromDTO {
    private Long id;
    @FormField(label = "员工id")
    private Long applicantId;
    private String applicantName;
    @FormField(label = "部门id")
    private Long deptId;
    private String deptName;
    @FormField(label = "职位id")
    private String postIds;
    private String postName;
    @FormField(label = "电话")
    private String phone;
    // 入职日期
    private LocalDateTime enterDate;
    @FormField(label = "请假类型")
    private Long holidayId;
    private String holidayName;
    // 假期余额
    @FormField(label = "假期余额")
    private Float holidayBalance;
    @FormField(label = "请假时间")
    private List<String> leaveTime;
    // 请假开始时间
    private LocalDateTime leaveStartTime;
    // 请假结束时间
    private LocalDateTime leaveEndTime;
    // 请假时长
    @FormField(label = "请假时长")
    private Float leaveNumber;
    // 休假地址
    @FormField(label = "休假地点")
    private String leaveLocation;
    // 扣除的记录id
    private String deadlineIds;
    // 文件的主键
    private Long fileKey;
    // 请假单(审批)状态
    private String status;
    // 流程实例id
    private String processInstanceId;
    // 创建时间(流程发起时间）
    private LocalDateTime createTime;
    // 修改时间(流程结束时间)
    private LocalDateTime updateTime;

    public LocalDateTime getLeaveStartTime() {
        if (leaveStartTime != null) {
            return leaveStartTime;
        }
        if (leaveTime != null && leaveTime.size() > AttConstants.oneInt) {
            return DateUtils.parse(leaveTime.getFirst());
        }
        return null;
    }

    public LocalDateTime getLeaveEndTime() {
        if (leaveEndTime != null) {
            return leaveEndTime;
        }
        if (leaveTime != null && leaveTime.size() > AttConstants.oneInt) {
            return DateUtils.parse(leaveTime.get(AttConstants.oneInt));
        }
        return null;
    }

    public void checkNull() {
        if (leaveTime == null || leaveTime.size() <= AttConstants.oneInt) {
            throw new ServiceException("请假时间不能为空！");
        }
        if (applicantId == null) {
            throw new ServiceException("申请人不能为空！");
        }
        if (holidayId == null) {
            throw new ServiceException("假期类型不能为空！");
        }
        if (leaveNumber == null || leaveNumber <= AttConstants.zeroInt) {
            throw new ServiceException("请假时长必须大于0！");
        }
    }
}
