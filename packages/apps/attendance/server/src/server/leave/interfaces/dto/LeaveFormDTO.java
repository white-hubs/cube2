package com.guhecloud.cube.srv.gen.attendance.management.server.leave.interfaces.dto;

import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.domain.aggregate.Holiday;
import com.guhecloud.cube.srv.gen.attendance.management.server.leave.domain.domainservice.LeaveTimeDomainService;
import com.guhecloud.cube.srv.gen.attendance.management.server.leave.domain.entity.LeaveFormDetail;
import com.guhecloud.cube.srv.gen.attendance.management.server.overtime.annotation.FormField;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LeaveFormDTO {
    @FormField(label = "员工id")
    private Long employeeId;
    @FormField(label = "部门id")
    private Long departmentId;
    @FormField(label = "岗位id")
    private Long positionId;
    @FormField(label = "请假类型")
    private Holiday.HolidayType holidayType;
    /**
     * 时间区间格式: 2020-01-01 00:00:00,2020-01-01 12:00:00,TIME_RANGE
     * 日期上午格式: 2020-01-01,AM
     */
    @FormField(label = "请假时间")
    private String leaveTimeStr;

    @FormField(label = "休假地点")
    private String leaveLocation;

    public LeaveFormDetail.LeaveTime getLeaveTime() {
        return LeaveTimeDomainService.getLeaveTime(this.leaveTimeStr);
    }
}
