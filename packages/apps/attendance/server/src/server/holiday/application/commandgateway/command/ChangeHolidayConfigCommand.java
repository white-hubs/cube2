package com.guhecloud.cube.srv.gen.attendance.management.server.holiday.application.commandgateway.command;

import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.application.querygateway.dto.HolidayDetailDTO;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.domain.entity.LeaveRule;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.domain.valueobject.LeaveTimeConvert;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.domain.valueobject.RangeEnum;
import lombok.Data;

import jakarta.annotation.Nullable;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

/*
 * 修改假期配置规则
 */
@Data
public class ChangeHolidayConfigCommand {
    /**
     * 假期id
     */
    @NotNull
    private Long id;
    @NotBlank
    private String name;
    /**
     * 为空表示适用于全部
     */
    @Nullable
    private Set<Long> employeeIds;
    /**
     * 入职时长限制
     */
    @NotNull
    private Integer timeOfInductionLimit;

    @NotNull
    private LeaveTimeConvert.ConvertWay convertWay;
    // 最小请假单位
    @NotNull
    private LeaveRule.MinimumLeaveTimeUnitConvert minimumLeaveTimeUnitConvert;

    @NotNull
    private Boolean isNeedLimitQuota;

    /**
     * 适用范围
     */
    private RangeEnum ranges;

    private HolidayDetailDTO.GrantRuleConfig grantRuleConfig;
    @NotNull
    private HolidayDetailDTO.ExpiryDateConfig expiryDateConfig;

    public HolidayDetailDTO.GrantRuleConfig getGrantRuleConfig() {
        if (!isNeedLimitQuota) {
            return null;
        }

        return this.grantRuleConfig;
    }

    public HolidayDetailDTO.ExpiryDateConfig getExpiryDateConfig() {
        if (!this.isNeedLimitQuota) {
            return null;
        }

        return this.expiryDateConfig;
    }

}
