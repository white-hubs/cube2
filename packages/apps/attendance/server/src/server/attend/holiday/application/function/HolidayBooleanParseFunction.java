package com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.application.function;

import com.guhecloud.platform.base.util.ObjectUtils;
import com.mzt.logapi.service.IParseFunction;
import org.springframework.stereotype.Component;

/**
 * @Author: lijie
 * @CreateTime: 2024-01-29  11:12
 * @Description:
 * @Version: 1.0
 */
@Component
public class HolidayBooleanParseFunction implements IParseFunction {
    @Override
    public String functionName() {
        return "HOLIDAY_BOOLEAN";
    }

    @Override
    public String apply(Object value) {
        String name = "否";
        if (ObjectUtils.isNotEmpty(value)) {
            name = "true".equals(String.valueOf(value)) ? "是" : "否";
        }
        return name;
    }
}
