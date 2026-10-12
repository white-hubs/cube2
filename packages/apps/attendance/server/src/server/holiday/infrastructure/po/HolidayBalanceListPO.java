package com.guhecloud.cube.srv.gen.attendance.management.server.holiday.infrastructure.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("holiday_balance_list")
public class HolidayBalanceListPO extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    private Long holidayTypeId;
    private Long userId;
    private String remainBalance;
    @TableField("`show`")
    private Boolean show;
    private String employeeName;
    private Long employeeOrganization;
    private LocalDateTime timeOfInduction;
    private Long attendanceGroupIdOfArrange;
}
