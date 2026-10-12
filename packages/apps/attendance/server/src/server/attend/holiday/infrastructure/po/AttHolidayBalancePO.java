package com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.infrastructure.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import com.mzt.logapi.starter.annotation.DiffLogField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 人员假期余额
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("att_holiday_balance")
@EqualsAndHashCode(callSuper = true)
public class AttHolidayBalancePO extends BasicEnhancedTenantEntity<Long> {
    @DiffLogField(name = "余额id")
    private Long id;
    /**
     * 员工id
     */
    @DiffLogField(name = "员工id")
    private Long staffId;
    /**
     * 假期类型
     */
    @DiffLogField(name = "假期id")
    private Long holidayRuleId;
    /**
     * 不限额
     */
    private Boolean notLimit;
    /**
     * 可用余额
     */
    @DiffLogField(name = "余额")
    private Long availableBalance;
    /**
     * 占用余额
     */
    @DiffLogField(name = "占用余额")
    private Long occupyBalance;
    /**
     * 余额修改备注(最后一次修改的备注)
     */
    @DiffLogField(name = "备注")
    private String updateRemark;
}
