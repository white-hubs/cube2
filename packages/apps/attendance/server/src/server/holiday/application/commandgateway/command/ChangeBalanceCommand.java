package com.guhecloud.cube.srv.gen.attendance.management.server.holiday.application.commandgateway.command;

import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.domain.aggregate.Holiday;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotNull;

@Getter
@Setter
public class ChangeBalanceCommand {
    @NotNull(message = "员工不能为空")
    private Long employeeId;
    @NotNull(message = "余额不能为空")
    private Float balance;
    @NotNull(message = "假期类型不能为空")
    private Holiday.HolidayType holidayType;

    @NotNull(message = "原始假期余额不能为空")
    private Float originalBalance;

    /**
     * (修改余额时的)备注
     */
    @Length(max = 200, message = "备注最大长度为200")
    private String remark;

    public Float getBalance() {
        if (this.holidayType == Holiday.HolidayType.COMPENSATORY_LEAVE) {
            return balance;
        }
        return balance * 8;
    }
}
