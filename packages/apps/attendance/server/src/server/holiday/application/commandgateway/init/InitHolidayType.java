package com.guhecloud.cube.srv.gen.attendance.management.server.holiday.application.commandgateway.init;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.domain.aggregate.Holiday;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.domain.entity.HolidayDetail;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.domain.entity.LeaveRule;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.domain.repository.HolidayRepository;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.domain.valueobject.ExpiryDate;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.domain.valueobject.LeaveTimeConvert;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.domain.valueobject.NewEmployeeLeaveRule;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/*
 *
 * 初始化假期类型
 * 租户初始化数据
 */
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class InitHolidayType {

    private final HolidayRepository holidayRepository;

    /**
     * todo 需要防止重新初始化
     */
    public void execute() {
        for (Holiday.HolidayType holidayType : Holiday.HolidayType.values()) {
            Holiday holiday = Holiday.builder()
                    .holidayDetail(new HolidayDetail(holidayType.getLabel()))
                    .holidayGrantRule(null)
                    .holidayType(holidayType)
                    .id(IdWorker.getId())
                    .leaveRule(
                            LeaveRule.builder()
                                    .leaveTimeConvert(new LeaveTimeConvert(LeaveTimeConvert.ConvertWay.CALENDAR_DAY))
                                    .minimumLeaveTimeUnitConvert(new LeaveRule.MinimumLeaveTimeUnitConvert(
                                            LeaveRule.MinimumLeaveTimeUnit.DAY,
                                            Arrays.asList(new LeaveRule.DayConvert("DAY", 8.0f),
                                                          new LeaveRule.HalfDayConvert("HALF_DAY", 4.0f, 4.0f))
                                    )).newEmployeeLeaveRule(new NewEmployeeLeaveRule(0))
                                    .build()
                              )
                    .expiryDate(ExpiryDate.builder().expiryDateType(ExpiryDate.ExpiryDateType.ALONG).build())
                    .build();
            holidayRepository.save(holiday);
        }
    }
}
