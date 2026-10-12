package com.guhecloud.cube.srv.gen.attendance.management.server.holiday.infrastructure.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@TableName("holiday_user_range")
@AllArgsConstructor
@NoArgsConstructor
public class HolidayUserRangPO {
    private Long holidayTypeId;
    private Long userId;
    private String employeeName;
    private Long employeeOrganization;
    private LocalDateTime timeOfInduction;
}
