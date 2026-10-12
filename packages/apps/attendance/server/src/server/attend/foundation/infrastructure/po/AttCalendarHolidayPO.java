package com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.infrastructure.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 日历假期
 */
@Data
@TableName("att_calendar_holiday")
@EqualsAndHashCode(callSuper = true)
public class AttCalendarHolidayPO extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    private LocalDateTime dayTime;
    private String dayFormat;
    private Integer year;
    private Integer month;
    private Integer week;
    private Integer day;
    // 休息日true表示休息，false表示上班(除周末和节日，周一到五和调班都是上班)
    private Boolean holiday;
    // (0, 1, 2, 3),  日期类型：分别表示 工作日、周末、节假日、调班。
    private Integer holidayType;
}
