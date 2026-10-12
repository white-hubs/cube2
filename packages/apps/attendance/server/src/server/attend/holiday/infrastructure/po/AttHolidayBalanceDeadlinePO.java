package com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.infrastructure.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 假期余额有效期
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("att_holiday_balance_deadline")
public class AttHolidayBalanceDeadlinePO extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    private Long holidayRuleId;
    private Long staffId;
    /**
     * 数量
     */
    private Long balance;
    /**
     * 截至日期(余额有效期
     */
    private LocalDateTime deadline;
    /**
     * 正常（被预扣的就不正常
     */
    private Boolean normal;
    /**
     * 父级id(表示从该条数据上拆分出来的)
     */
    private Long parentId;
}
