package com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.application.function;

import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.domain.enums.AttHolidayGrantWayEnum;
import com.mzt.logapi.service.IParseFunction;
import org.springframework.stereotype.Component;

/**
 * @Author: lijie
 * @CreateTime: 2024-01-29  11:12
 * @Description:
 * @Version: 1.0
 */
@Component
public class HolidayGrantWayParseFunction implements IParseFunction {
    @Override
    public String functionName() {
        return "HOLIDAY_GRANT_WAY";
    }

    @Override
    public String apply(Object value) {
        AttHolidayGrantWayEnum grantWayEnum = null;
        if (ObjectUtils.isNotEmpty(value)) {
            grantWayEnum = AttHolidayGrantWayEnum.valueOf(String.valueOf(value));
        }
        return ObjectUtils.isNotEmpty(grantWayEnum) ? grantWayEnum.getValue() : "空";
    }
}
