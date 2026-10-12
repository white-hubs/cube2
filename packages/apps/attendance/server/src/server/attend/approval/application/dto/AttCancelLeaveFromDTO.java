package com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.application.dto;

import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.common.AttConstants;
import com.guhecloud.cube.srv.gen.attendance.management.server.overtime.annotation.FormField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 销假单
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AttCancelLeaveFromDTO {
    private Long id;
    @FormField(label = "员工id")
    private Long applicantId;
    private String applicantName;
    @FormField(label = "部门id")
    private Long deptId;
    private String deptName;
    private Long holidayId;
    private String holidayName;
    // 关联的请假单对象（json
    private String cancelLeaveFrom;
    // 请假单id
    @FormField(label = "关联请假单")
    private Long askLeaveFromId;
    // 销假时间
    @FormField(label = "销假时间")
    private List<String> cancelTime;
    private LocalDateTime cancelStartTime;
    private LocalDateTime cancelEndTime;
    // 销假时长
    @FormField(label = "销假时长")
    private Float cancelLeaveNumber;
    // 销假备注
    private String cancelRemark;
    // 文件的主键
    private Long fileKey;
    // 销假单(审批)状态
    private String status;
    // 流程实例id
    private String processInstanceId;

    public LocalDateTime getCancelStartTime() {
        if (cancelStartTime != null) {
            return cancelStartTime;
        }
        if (cancelTime != null && cancelTime.size() > AttConstants.oneInt) {
            return DateUtils.parse(cancelTime.get(AttConstants.zeroInt));
        }
        return null;
    }

    public LocalDateTime getCancelEndTime() {
        if (cancelEndTime != null) {
            return cancelEndTime;
        }
        if (cancelTime != null && cancelTime.size() > AttConstants.oneInt) {
            return DateUtils.parse(cancelTime.get(AttConstants.oneInt));
        }
        return null;
    }

}
