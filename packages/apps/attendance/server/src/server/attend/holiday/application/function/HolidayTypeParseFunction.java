package com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.application.function;

import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.domain.enums.AttLegalHolidayEnum;
import com.mzt.logapi.service.IParseFunction;
import org.springframework.stereotype.Component;

/**
 * @Author: lijie
 * @CreateTime: 2024-01-29  11:12
 * @Description:
 * @Version: 1.0
 */
@Component
public class HolidayTypeParseFunction implements IParseFunction {
    @Override
    public String functionName() {
        return "HOLIDAY_TYPE";
    }

    @Override
    public String apply(Object value) {
        AttLegalHolidayEnum holidayEnum = null;
        if (ObjectUtils.isNotEmpty(value)) {
            holidayEnum = AttLegalHolidayEnum.valueOf(String.valueOf(value));
        }
        return ObjectUtils.isNotEmpty(holidayEnum) ? holidayEnum.getValue() : "空";
    }
}
