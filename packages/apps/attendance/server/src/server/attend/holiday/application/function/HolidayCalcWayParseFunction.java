package com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.application.function;

import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.common.enums.AttDurationCalcWayEnum;
import com.mzt.logapi.service.IParseFunction;
import org.springframework.stereotype.Component;

/**
 * @Author: lijie
 * @CreateTime: 2024-01-29  11:12
 * @Description:
 * @Version: 1.0
 */
@Component
public class HolidayCalcWayParseFunction implements IParseFunction {
    @Override
    public String functionName() {
        return "HOLIDAY_CALC_WAY";
    }

    @Override
    public String apply(Object value) {
        AttDurationCalcWayEnum durationCalcWay = null;
        if (ObjectUtils.isNotEmpty(value)) {
            durationCalcWay = AttDurationCalcWayEnum.valueOf(String.valueOf(value));
        }
        return ObjectUtils.isNotEmpty(durationCalcWay) ? durationCalcWay.getValue() : "空";
    }
}
