package com.guhecloud.cube.srv.gen.attendance.management.server.leave.application.commandgateway.validator;

import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.application.querygateway.EmployeeHolidayQueryService;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.application.querygateway.HolidayQueryService;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.domain.aggregate.EmployeeHoliday;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.domain.aggregate.Holiday;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.domain.valueobject.HolidayBalance;
import com.guhecloud.cube.srv.gen.attendance.management.server.leave.domain.aggregate.LeaveForm;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.CollectionUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author pengjian
 * @date 2022/11/18 18:59
 * @description 余额验证
 */
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BalanceValidator implements SubmitLeaveFormValidator {

    private final EmployeeHolidayQueryService employeeHolidayQueryService;
    private final HolidayQueryService holidayQueryService;

    @Override
    public boolean verify(LeaveForm leaveForm) {

        // 如果是不限额跳过
        //如果是不限额假期不用预扣
        Holiday holiday = holidayQueryService.getByType(leaveForm.getDetail().getLeaveType());
        if (!holiday.isQuotaLimited()) {
            return true;
        }

        // 获取假期余额
        EmployeeHoliday employeeHoliday = employeeHolidayQueryService.getById(leaveForm.getDetail().getUserId());
        List<HolidayBalance> balances = employeeHoliday.getHolidayBalances().stream()
                .filter(holidayBalance -> holidayBalance.getHolidayType() == leaveForm.getDetail().getLeaveType())
                .toList();

        if (CollectionUtils.isEmpty(balances)) {
            throw new ServiceException("假期余额不足");
        }

        HolidayBalance holidayBalance = balances.getFirst();
        return holidayBalance.getRemainingBalance() >= leaveForm.getDetail().getLeaveHour();
    }
}
