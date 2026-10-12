package com.guhecloud.cube.srv.gen.attendance.management.server.holiday.application.commandgateway.command;

import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.domain.aggregate.Holiday;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

/**
 * 发放假期
 */
@Getter
public class GrantHolidayCommand {
    @NotNull
    private Long employeeId;
    /**
     * 发放的假期时长，单位小时，最多1位小数
     */
    @NotNull
    @Pattern(regexp = "^([1-9]d*)(.d{1})$", message = "只能保留1位小数")
    private Float number;

    /**
     * 发放的假期类型
     */
    @NotNull
    private Holiday.HolidayType holidayType;

    /**
     * 发放备注
     */
    @Setter
    private String remark;

    public GrantHolidayCommand(Long employeeId, Float number, Holiday.HolidayType holidayType) {
        if (number <= 0) {
            throw new RuntimeException("非法假期数量");
        }
        this.employeeId = employeeId;
        this.number = number;
        this.holidayType = holidayType;
    }

}
